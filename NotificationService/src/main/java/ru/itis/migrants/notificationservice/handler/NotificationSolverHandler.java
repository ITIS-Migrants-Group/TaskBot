package ru.itis.migrants.notificationservice.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.itis.migrants.notificationservice.dto.NotificationResponse;

import java.time.OffsetDateTime;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class NotificationSolverHandler {
    private final List<SaveSolver> solvers;

    public boolean solve(NotificationResponse response, OffsetDateTime time) {
        for (SaveSolver saveSolver : solvers) {
            if (saveSolver.support(response)) {
                log.debug("{} solve response: {}", saveSolver.getClass().getSimpleName(), response);
                return saveSolver.solve(response, time);
            }
        }
        log.warn("Response didn't solved, response not supported: {}", response);
        return false;
    }
}
