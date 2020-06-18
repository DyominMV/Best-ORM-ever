package bestorm.impl.util;

import java.lang.ref.Cleaner;
import java.lang.ref.Cleaner.Cleanable;
import java.util.WeakHashMap;

public abstract class Destructable implements AutoCloseable {

  private static final Cleaner cleaner = Cleaner.create();
  private static final WeakHashMap<Destructable, Cleanable> cleanables = new WeakHashMap<>();

  static {
    Runtime.getRuntime().addShutdownHook(new Thread(() -> {
      for (Cleanable cleanable : cleanables.values()) {
        cleanable.clean();
      }
    }));
  }

  private final Cleanable cleanable;
  private final Runnable destructor;

  public Destructable(final Runnable destructor, boolean cleanAnyway) {
    cleanable = cleaner.register(this, destructor);
    if (cleanAnyway) {
      cleanables.put(this, cleanable);
    }
    this.destructor = destructor;
  }

  public Runnable getDestructor() {
    return destructor;
  }

  public Destructable(final Runnable destructor) {
    cleanable = cleaner.register(this, destructor);
    this.destructor = destructor;
  }

  @Override
  public final void close() throws Exception {
    cleanable.clean();
  }

}