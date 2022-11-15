package com.go.ogamprj.controller.admin;

import com.go.ogamprj.mapper.DashboardMapper;
import com.go.ogamprj.sevice.DashboardService;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@Controller
public class Admin_DashboardController {

    @Autowired
    DashboardService dashboardService;
    @Autowired
    DashboardMapper dashboardMapper;

    @RequestMapping("/adminMain") // application root
    public String main(Model model) {
        model.addAttribute("users", dashboardService.users() + "명");
        model.addAttribute("removeUser", dashboardService.removeUser() + "명");
        model.addAttribute("diary", dashboardService.diary() + "개");
        model.addAttribute("reply", dashboardService.blackcnt().size() + "개");

        return "admin/main";
    }

    @RequestMapping("/emotion")
    @ResponseBody
    public List<EmotionStatus> emotionChart() {
        ArrayList<String> emotion = dashboardService.emotionList();
        ArrayList<Integer> cnt = dashboardService.cntList();
        String[] emoji = {"😭", "😄", "😡", "😔", "😥", "🥰"};
        List<EmotionStatus> map = new ArrayList<>();
        for (int i = 0; i < emotion.size(); i++) {
            map.add(EmotionStatus.builder().name(emotion.get(i) + emoji[i]).score(cnt.get(i)).build());
        }

        return map;
    }

    @RequestMapping("/diaryCnt")
    @ResponseBody
    public int diaryCnt() {
        int dCnt = dashboardService.diaryCnt();
//        System.out.println(dCnt);
        return dCnt;
    }

    @RequestMapping("/memberCnt")
    @ResponseBody
    public int memberCnt() {
        int mCnt = dashboardService.memberCnt();
//        System.out.println(mCnt);
        return mCnt;
    }


}

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
class EmotionStatus {
    String name;
    Integer score;
}

