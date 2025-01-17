package com.go.ogamprj.controller.user;

import com.go.ogamprj.dto.Block;
import com.go.ogamprj.dto.Board;
import com.go.ogamprj.sevice.BlockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;

@Controller
public class User_BlockController {
    @Autowired
    BlockService blockService;

    public static void init(HttpServletResponse response) {
        response.setContentType("text/html; charset=euc-kr");
        response.setCharacterEncoding("euc-kr");
    }

    @RequestMapping("/blockPage")
    public String blockPage(HttpServletRequest request, Model model) {
        Object loginUser = request.getSession().getAttribute("loginUser");
        if(loginUser == null){
            return "redirect:/";
        }else {
            List<HashMap<String, Object>> blockList = blockService.blockList(loginUser.toString());
            model.addAttribute("blockList", blockList);
            model.addAttribute("blockCnt", blockList.size());

            return "user/noticePage/blockList";
        }
    }

    @RequestMapping("/blockDel")
    public String blockDel(HttpServletRequest request, @RequestParam int block_seq) {
        Object loginUser = request.getSession().getAttribute("loginUser");
        if(loginUser == null){
            return "redirect:/";
        }else {
            blockService.blockDel(block_seq);
            return "redirect:/blockPage";
        }
    }

    @RequestMapping("/blockPlus")
    public String blockPlus(HttpServletRequest request
                            , HttpServletResponse response
                            , @RequestParam(defaultValue = "") String member_nick) throws IOException {

        Object loginUser = request.getSession().getAttribute("loginUser");
        if(loginUser == null){
            return "redirect:/";
        }else {
            init(response);
            PrintWriter out = response.getWriter();

            String block_email = "";
            if (!member_nick.isEmpty()) {
                block_email = blockService.findId(member_nick);
                if (block_email == null || block_email.isEmpty()) {
                    return "redirect:/blockPage";
                }
                if (blockService.doubleBlock(new Block(loginUser.toString(), block_email)) == 1) {
                    out.println("<script>alert('이미 차단한 유저입니다'); location.href='/blockPage'</script>");
                    out.flush();
                } else {
                    blockService.blockPlus(new Block(0, loginUser.toString(), block_email));
                }

            }
            return "redirect:/blockPage";
        }
    }

}
