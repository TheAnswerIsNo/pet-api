package com.wait.app.service;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjUtil;

import com.wait.app.domain.dto.order.OrderListDTO;
import com.wait.app.domain.dto.order.OrderPayDTO;
import com.wait.app.domain.dto.wechatPayCallback.DecryptedSuccessData;
import com.wait.app.domain.entity.OrderDetail;
import com.wait.app.domain.entity.TOrder;
import com.wait.app.domain.enumeration.OrderStatusEnum;
import com.wait.app.domain.enumeration.PrefixEnum;
import com.wait.app.domain.enumeration.WeChatInfoEnum;
import com.wait.app.domain.param.order.OrderSubmitParam;
import com.wait.app.repository.OrderDetailRepository;
import com.wait.app.repository.OrderRepository;
import com.wechat.pay.java.core.Config;
import com.wechat.pay.java.service.payments.jsapi.JsapiServiceExtension;
import com.wechat.pay.java.service.payments.jsapi.model.Amount;
import com.wechat.pay.java.service.payments.jsapi.model.Payer;
import com.wechat.pay.java.service.payments.jsapi.model.PrepayRequest;
import com.wechat.pay.java.service.payments.jsapi.model.PrepayWithRequestPaymentResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 *
 * @author 天
 * Time: 2024/10/5 2:43
 */
@Service
public class OrderService {

    private final OrderRepository orderRepository;

    private final OrderDetailRepository orderDetailRepository;

    private Config payConfig;

    private final RedisTemplate<String, Object> objectRedisTemplate;

    @Autowired
    public OrderService(OrderRepository orderRepository, OrderDetailRepository orderDetailRepository, RedisTemplate<String, Object> objectRedisTemplate) {
        this.orderRepository = orderRepository;
        this.orderDetailRepository = orderDetailRepository;
        this.objectRedisTemplate = objectRedisTemplate;
    }

    /**
     * 提交订单
     * @param orderSubmitParam orderSubmitParam
     * @param userId 用户id
     */
    @Transactional(rollbackFor = Exception.class)
    public OrderPayDTO submit(OrderSubmitParam orderSubmitParam, String userId) {
//        User user = (User) StpUtil.getSession().get(SaSession.USER);
        // 1.创建订单参数
        TOrder order = TOrder.builder().userId(userId)
                .addressId(orderSubmitParam.getAddressId()).build();
        orderRepository.save(order);

        AtomicReference<BigDecimal> totalPrice = new AtomicReference<>(BigDecimal.ZERO);
        AtomicReference<Integer> totalNumber = new AtomicReference<>(0);

        List<OrderSubmitParam.OrderSubmitDetail> orderSubmitDetailList = orderSubmitParam.getOrderSubmitDetailList();
        List<OrderDetail> orderDetails = BeanUtil.copyToList(orderSubmitDetailList, OrderDetail.class);
        orderDetails.forEach(item -> {
            item.setOrderId(order.getId());
            totalPrice.set(totalPrice.get().add(item.getPrice().multiply(new BigDecimal(item.getNumber()))));
            totalNumber.updateAndGet(v -> v + item.getNumber());
        });
        orderDetailRepository.saveBatch(orderDetails);

        order.setTotalNumber(totalNumber.get());
        order.setTotalPrice(totalPrice.get());
        orderRepository.updateById(order);

        //TODO 2.请求微信预下单接口获取支付参数
//        PrepayWithRequestPaymentResponse prepayOrder = createPrepayOrder(order, user.getOpenId());
        // 设置订单时间15分钟
        objectRedisTemplate.opsForValue().set(PrefixEnum.ORDER.getValue() + order.getId(),new PrepayWithRequestPaymentResponse(), Duration.ofMinutes(15));

        return new OrderPayDTO(null, order.getId());
    }

    private PrepayWithRequestPaymentResponse createPrepayOrder(TOrder order, String openId) {

        JsapiServiceExtension service = new JsapiServiceExtension.Builder().config(payConfig).build();
        // request.setXxx(val)设置所需参数，具体参数可见Request定义
        PrepayRequest request = new PrepayRequest();
        Amount amount = new Amount();
        BigDecimal total = order.getTotalPrice().multiply(new BigDecimal(100));
        amount.setTotal(total.intValueExact());
        request.setAmount(amount);
        request.setAppid(WeChatInfoEnum.APPID.getValue());
        request.setMchid(WeChatInfoEnum.MERCHANT_ID.getValue());
        request.setDescription("jc商品");
        request.setNotifyUrl(WeChatInfoEnum.NOTIFYURL.getValue());
        request.setOutTradeNo(order.getId());
        Payer payer = new Payer();
        payer.setOpenid(openId);
        request.setPayer(payer);
        // response包含了调起支付所需的所有参数，可直接用于前端调起支付
        return service.prepayWithRequestPayment(request);
    }

    /**
     * 取消订单
     * @param orderId orderId
     */
    public void cancelOrder(String orderId) {
        String orderKey = PrefixEnum.ORDER.getValue() + orderId;
        objectRedisTemplate.delete(orderKey);

        // 关闭订单
//        JsapiServiceExtension service = new JsapiServiceExtension.Builder().config(payConfig).build();
//        CloseOrderRequest closeOrderRequest = new CloseOrderRequest();
//        closeOrderRequest.setOutTradeNo(orderId);
//        closeOrderRequest.setMchid(WeChatInfoEnum.MERCHANT_ID.getValue());
//        service.closeOrder(closeOrderRequest);

        TOrder order = TOrder.builder().id(orderId).status(OrderStatusEnum.DDQX.getValue()).build();
        orderRepository.updateById(order);
    }

    /**
     * 吊起wx支付
     * @param orderId orderId
     */
    public PrepayWithRequestPaymentResponse payOrder(String orderId) {
        // TODO 付款
        // 修改订单状态
        TOrder order = TOrder.builder()
                .id(orderId)
                .status(OrderStatusEnum.WFK.getValue())
                .build();
        orderRepository.updateById(order);

//        String orderKey = PrefixEnum.ORDER.getValue() + orderId;
//        RBucket<Object> bucket = redissonClient.getBucket(orderKey);
//        String prepayResponse = (String)bucket.get();
//        return JSONUtil.toBean(prepayResponse, PrepayWithRequestPaymentResponse.class);
        return new PrepayWithRequestPaymentResponse();
    }

    /**
     * 支付回调
     * @param decryptedSuccessData decryptedSuccessData
     */
    public void payCallback(DecryptedSuccessData decryptedSuccessData) {
        String orderId = decryptedSuccessData.getOutTradeNo();
        // 修改订单状态
        TOrder order = TOrder.builder()
                .id(orderId)
                .status(OrderStatusEnum.SHZ.getValue())
                .build();
        orderRepository.updateById(order);
    }

    /**
     * 订单列表
     * @param userId userId
     * @param status 状态
     */
    public List<OrderListDTO> list(String userId, Integer status) {
        List<TOrder> orderList = orderRepository.lambdaQuery()
                .eq(TOrder::getUserId, userId)
                .eq(ObjUtil.isNotEmpty(status), TOrder::getStatus, status).list();
        List<OrderListDTO> list = BeanUtil.copyToList(orderList, OrderListDTO.class);
        List<String> orderIds = orderList.stream().map(TOrder::getId).toList();
        if (CollUtil.isEmpty(orderIds)) {
            return list;
        }
        Map<String, List<OrderDetail>> orderDetailMap = orderDetailRepository.lambdaQuery()
                .in(OrderDetail::getOrderId, orderIds)
                .list().stream().collect(Collectors.groupingBy(OrderDetail::getOrderId));
        list.forEach(item -> {
            List<OrderListDTO.OrderListDetail> orderListDetails = BeanUtil.copyToList(orderDetailMap.get(item.getId()), OrderListDTO.OrderListDetail.class);
            item.setOrderListDetailList(orderListDetails);
        });
        return list;
    }
}
