package me.wuwenbin.noteblogv4.web.frontend;

import me.wuwenbin.noteblogv4.dao.repository.ArticleRepository;
import me.wuwenbin.noteblogv4.dao.repository.CateRepository;
import me.wuwenbin.noteblogv4.dao.repository.ParamRepository;
import me.wuwenbin.noteblogv4.model.constant.NoteBlogV4;
import me.wuwenbin.noteblogv4.model.entity.NBArticle;
import me.wuwenbin.noteblogv4.model.pojo.bo.ArticleQueryBO;
import me.wuwenbin.noteblogv4.model.pojo.framework.NBR;
import me.wuwenbin.noteblogv4.model.pojo.framework.Pagination;
import me.wuwenbin.noteblogv4.service.content.ArticleService;
import me.wuwenbin.noteblogv4.web.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import static me.wuwenbin.noteblogv4.model.constant.NoteBlogV4.ParamValue.PAGE_MODERN_BUTTON;
import static me.wuwenbin.noteblogv4.model.constant.NoteBlogV4.ParamValue.PAGE_MODERN_DEFAULT;

/**
 * created by Wuwenbin on 2018/7/31 at 21:33
 *
 * @author wuwenbin
 */
@Controller
public class IndexController extends BaseController {

    private final ParamRepository paramRepository;
    private final ArticleRepository articleRepository;
    private final CateRepository cateRepository;
    private final ArticleService articleService;

    @Autowired
    public IndexController(ParamRepository paramRepository, ArticleRepository articleRepository, CateRepository cateRepository, ArticleService articleService) {
        this.paramRepository = paramRepository;
        this.articleRepository = articleRepository;
        this.cateRepository = cateRepository;
        this.articleService = articleService;
    }

    @RequestMapping(value = {"", "/index"})
    public String index(Model model, Pagination<NBArticle> pagination, ArticleQueryBO articleQueryBO) {
        String pageModern = paramRepository.findByName(NoteBlogV4.Param.PAGE_MODERN).getValue();
        model.addAttribute("articleCount", articleRepository.count());
        model.addAttribute("cateList", cateRepository.findAll());
        model.addAttribute("page1Articles", articleService.findBlogArticles(getPageable(pagination), articleQueryBO));
        return handleStyle(
                "frontend/index/index_simple",
                () -> {
                    if (PAGE_MODERN_DEFAULT.equalsIgnoreCase(pageModern)) {
                        return "frontend/index/index_flow";
                    } else if (PAGE_MODERN_BUTTON.equalsIgnoreCase(pageModern)) {
                        return "frontend/index/index_pagination";
                    } else {
                        return "redirect:/error?errorCode=404";
                    }
                }, paramRepository
        );
    }

    @RequestMapping(value = {"/next", "/index/next"})
    @ResponseBody
    public NBR nextPageArticle(Pagination<NBArticle> pagination, ArticleQueryBO articleQueryBO) {
        Pageable pageable = getPageable(pagination);
        Page<NBArticle> page = articleService.findBlogArticles(pageable, articleQueryBO);
        return NBR.ok(page);
    }

}
