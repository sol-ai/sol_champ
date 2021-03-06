package sol_engine.module;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public abstract class Module {
    private final Logger logger = LoggerFactory.getLogger(Module.class);

    private ModulesHandler modulesHandeler = null;
    Set<Class<? extends Module>> usingModules = new HashSet<>();
    boolean simulationShouldTerminate = false;

    abstract public void onSetup();

    abstract public void onStart();

//    protected void onStartEnd() {
//    }

    abstract public void onEnd();

    abstract public void onUpdate();

    @SafeVarargs
    protected final void usingModules(Class<? extends Module>... moduleTypes) {
        usingModules(Arrays.asList(moduleTypes));
    }

    protected final void usingModules(Collection<Class<? extends Module>> moduleTypes) {
        usingModules.addAll(moduleTypes);
    }

    public final void internalSetup(ModulesHandler modulesHandler) {
        this.modulesHandeler = modulesHandler;
        onSetup();
    }

    public final void internalStart() {
        onStart();
    }

//    public final void internalStartEnd() {
//        onStartEnd();
//    }

    public final void internalUpdate() {
        onUpdate();
    }

    public final void internalEnd() {
        onEnd();
    }

    public final <T extends Module> T getModule(Class<T> moduleType) {
        return this.modulesHandeler.getModule(moduleType);
    }

    public void simulationShouldTerminate() {
        simulationShouldTerminate = true;
    }
}
