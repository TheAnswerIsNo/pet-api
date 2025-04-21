package com.wait.app.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;

import com.wait.app.domain.dto.dict.DictListDTO;
import com.wait.app.domain.entity.Dict;
import com.wait.app.domain.param.dict.DictSaveParam;
import com.wait.app.repository.DictRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

/**
 * @author 天
 * Time: 2024/9/10 17:13
 */
@Service
public class DictService {

    private final DictRepository dictRepository;

    @Autowired
    public DictService(DictRepository dictRepository) {
        this.dictRepository = dictRepository;
    }


    /**
     * 获取指定字典列表
     * @param type type
     * @return List<DictListDTO>
     */
    public List<DictListDTO> list(String type) {
        List<Dict> dictList = dictRepository.lambdaQuery().eq(StrUtil.isNotEmpty(type),Dict::getType, type)
                .orderByDesc(Dict::getCreateTime)
                .orderByDesc(Dict::getSort).list();
        List<DictListDTO> dictListDTOList = BeanUtil.copyToList(dictList, DictListDTO.class)
                .stream().sorted(Comparator.comparing(DictListDTO::getSort).reversed()).toList();
        List<DictListDTO> parentDict = dictListDTOList.stream().filter(item -> StrUtil.isEmpty(item.getParentId())).toList();
        parentDict.forEach(item -> buildDictList(item, dictListDTOList));
        return parentDict;
    }

    /**
     * 递归构造树
     * @param parentDict parentDict
     * @param dictListDTOList dictListDTOList
     */
    private void buildDictList(DictListDTO parentDict,List<DictListDTO> dictListDTOList){
        dictListDTOList.forEach(dictListDTO -> {
            if (dictListDTO.getParentId().equals(parentDict.getId())){
                parentDict.getChildren().add(dictListDTO);
                buildDictList(dictListDTO,dictListDTOList);
            }
        });
    }

    /**
     * 保存字典
     * @param dictSaveParam dictSaveParam
     */
    @Transactional(rollbackFor = Exception.class)
    public void save(DictSaveParam dictSaveParam) {
        Dict dict = BeanUtil.copyProperties(dictSaveParam, Dict.class);
        dictRepository.saveOrUpdate(dict);
    }

    /**
     * 删除字典
     * @param id id
     */
    public void delete(String id) {
        dictRepository.removeById(id);
    }
}
