package com.wait.app.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.wait.app.domain.dto.goods.GoodsListDTO;
import com.wait.app.domain.entity.Attachment;
import com.wait.app.domain.entity.Goods;
import com.wait.app.domain.enumeration.AttachmentEnum;
import com.wait.app.domain.param.goods.GoodsListParam;
import com.wait.app.domain.param.goods.GoodsSaveParam;
import com.wait.app.repository.GoodsRepository;
import com.wait.app.utils.page.PageUtil;
import com.wait.app.utils.page.ResponseDTOWithPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 * @author 天
 * Time: 2025/4/23 13:47
 */
@Service
public class GoodsService {

    private final GoodsRepository goodsRepository;

    private final AttachmentService attachmentService;

    @Autowired
    public GoodsService(GoodsRepository goodsRepository, AttachmentService attachmentService) {
        this.goodsRepository = goodsRepository;
        this.attachmentService = attachmentService;
    }

    public ResponseDTOWithPage<GoodsListDTO> list(GoodsListParam goodsListParam) {
        PageUtil.startPage(goodsListParam,true, Goods.class);
        List<Goods> list = goodsRepository.lambdaQuery()
                .eq(StrUtil.isNotBlank(goodsListParam.getDictId()), Goods::getDictId, goodsListParam.getDictId())
                .orderByDesc(Goods::getCreateTime)
                .list();
        if (CollUtil.isEmpty(list)){
            return new ResponseDTOWithPage<>(List.of(),0L);
        }
        List<GoodsListDTO> goodsList = BeanUtil.copyToList(list, GoodsListDTO.class);
        // 添加图片
        List<String> goodsIds = goodsList.stream().map(GoodsListDTO::getId).toList();
        Map<String, List<Attachment>> photoMap = attachmentService.getAttachment(AttachmentEnum.GOODS.getValue(), goodsIds).stream()
                .collect(Collectors.groupingBy(Attachment::getOwnerId));
        goodsList.forEach(item ->{
            List<Attachment> attachments = photoMap.getOrDefault(item.getId(), List.of());
            if (CollUtil.isNotEmpty(attachments)){
                List<String> photos = attachments.stream().map(attachment -> attachmentService.getAttachmentUrl(attachment.getObjName(), null)).toList();
                item.setPhotos(photos);
            }
        });

        return PageUtil.getListDTO(goodsList, goodsListParam);
    }

    @Transactional(rollbackFor = Exception.class)
    public void delete(String goodsId) {
        goodsRepository.removeById(goodsId);
        List<Attachment> attachment = attachmentService.getAttachment(AttachmentEnum.GOODS.getValue(), goodsId);
        if (CollUtil.isNotEmpty(attachment)){
            attachmentService.removeAttachment(AttachmentEnum.GOODS.getValue(), goodsId);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void save(GoodsSaveParam goodsSaveParam) {
        Goods goods = BeanUtil.copyProperties(goodsSaveParam, Goods.class);
        goodsRepository.saveOrUpdate(goods);
        if (CollUtil.isNotEmpty(goodsSaveParam.getPhotos())){
            if (StrUtil.isNotBlank(goodsSaveParam.getId())){
                attachmentService.removeAttachment(AttachmentEnum.GOODS.getValue(), goods.getId());
            }
                attachmentService.uploadAttachmentList(goodsSaveParam.getPhotos(),AttachmentEnum.GOODS.getValue(), goods.getId());
        }
    }
}
