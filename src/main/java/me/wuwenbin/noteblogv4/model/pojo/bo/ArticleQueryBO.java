package me.wuwenbin.noteblogv4.model.pojo.bo;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * created by Wuwenbin on 2018/9/10 at 下午2:19
 * @author wuwenbin
 */
@ToString
@Data
public class ArticleQueryBO implements Serializable {

    /**
     * 包括 标题、内容等
     */
    private String searchStr;
    private Long cateId;

}
