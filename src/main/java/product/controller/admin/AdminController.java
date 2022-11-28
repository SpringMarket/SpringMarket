package product.controller.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import product.response.Response;
import product.service.admin.AdminService;

import static product.response.Response.success;

@Slf4j
@RequiredArgsConstructor
@Controller
public class AdminController {

    private final AdminService adminService;

    // Warm UP -> Ranking Board PipeLine
    @GetMapping("/warmup/rank/pipe")
    public Response warmupRankingPipeLine() {
        adminService.warmupRankingPipeLine();
        return success("SUCCESS ^__^ !!");
    }

    // Warm UP -> Named Post PipeLine
    @GetMapping("/warmup/pipe")
    public Response warmupPipeLine() {
        adminService.warmupPipeLine();
        return success("SUCCESS ^__^ !!");
    }

    // Warm UP -> Ranking Board
    @GetMapping("/warmup/rank")
    public Response warmupRank() {
        adminService.warmupRank();
        return success("SUCCESS ^__^ !!");
    }

    // Warm UP -> Named Post
    @GetMapping("/warmup")
    public Response warmup() {
        adminService.warmup();
        return success("SUCCESS ^__^ !!");
    }
}

