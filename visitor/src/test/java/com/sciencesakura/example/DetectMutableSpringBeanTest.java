package com.sciencesakura.example;

import static org.junit.jupiter.api.Assertions.assertEquals;

import edu.umd.cs.findbugs.IFindBugsEngine;
import edu.umd.cs.findbugs.test.SpotBugsExtension;
import edu.umd.cs.findbugs.test.SpotBugsRunner;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Path;
import java.util.function.Consumer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(SpotBugsExtension.class)
class DetectMutableSpringBeanTest {

  Consumer<IFindBugsEngine> customization = engine -> {
    try {
      engine.addFilter("target/test-classes/include.xml", true);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  };

  @Test
  void run(SpotBugsRunner runner) {
    var bugs = runner.performAnalysis(customization,
        Path.of("target/test-classes/com/sciencesakura/example/test/BaseClass.class"),
        Path.of("target/test-classes/com/sciencesakura/example/test/MutableSpringBean.class"),
        Path.of("target/test-classes/com/sciencesakura/example/test/NonSpringBean.class"),
        Path.of("target/test-classes/com/sciencesakura/example/test/OtherClass.class")
    ).getCollection();
    bugs.forEach(b -> {
      System.err.println(b.getBugPattern().getType() + ' ' + b.getMessage() + ' ' + b.getPrimarySourceLineAnnotation());
    });
    assertEquals(9, bugs.size());
  }
}
