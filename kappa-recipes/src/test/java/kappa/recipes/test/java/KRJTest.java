package kappa.recipes.test.java;

import kappa.Directive;
import kappa.KappaSession;
import kappa.Path;
import kappa.coordinator.Job;

import static kappa.kafka.Directives.*;

import static kappa.Directives.response;

/**
 * Created by j0rd1 on 5/11/16.
 */
/*
public class KRJTest extends Job {

    @Override
    public KappaSession session() {
        return null;
    }

    @Override
    public String domain() {
        return "jtest";
    }

    public final Directive start = readLock(rl ->
        createKafkaTopic("topic-name", 2, 1).success(
            rl.toWriteLock().success( wl ->
                wl.write("started").success(
                    response("test-" + wl.id())
                )
            )
        )
    );
}
*/