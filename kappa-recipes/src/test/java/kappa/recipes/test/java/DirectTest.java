package kappa.recipes.test.java;

import kappa.KappaSession;
import kappa.Directive;
import kappa.coordinator.Spec;
import static kappa.Directives.*;

/**
 * Created by j0rd1 on 10/11/16.
 */
public class DirectTest extends Spec {

    @Override
    public KappaSession session() {
        return null;
    }

    @Override
    public String domain() {
        return null;
    }

    public final Directive start = writeLock().success(lock ->
        response("")
    );


}
