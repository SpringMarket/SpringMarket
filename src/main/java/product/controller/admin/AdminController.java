package product.controller.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import product.response.Response;
import product.service.admin.AdminService;

import static product.response.Response.success;

@Slf4j
@RequiredArgsConstructor
@RestController
public class AdminController {

    private final AdminService adminService;

    // Warm UP -> Ranking Board PipeLine
    @GetMapping("/warmup/rank/pipe/{categoryId}")
    public Response warmupRankingPipeLine(@PathVariable Long categoryId) {
        adminService.warmupRankingPipeLine(categoryId);
        return success("SUCCESS ^__^ !!");
    }

    // Warm UP -> Named Post PipeLine
    @GetMapping("/warmup/pipe/{categoryId}")
    public Response warmupPipeLine(@PathVariable Long categoryId) {
        adminService.warmupPipeLine(categoryId);
        return success("SUCCESS ^__^ !!");
    }

/*    // Warm UP -> Ranking Board
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
    }*/
}

