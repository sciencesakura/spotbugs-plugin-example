package com.sciencesakura.example;

import static org.junit.jupiter.api.Assertions.assertEquals;

import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.IFindBugsEngine;
import edu.umd.cs.findbugs.test.SpotBugsExtension;
import edu.umd.cs.findbugs.test.SpotBugsRunner;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Path;
import java.util.function.Consumer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(SpotBugsExtension.class)
class DetectHogeClassTest {

  Consumer<IFindBugsEngine> customization = engine -> {
    try {
      // SpotBugs標準のバグは検知しないようフィルタを指定
      engine.addFilter("target/test-classes/include.xml", true);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  };

  @Test
  @DisplayName("NGケース")
  void ng(SpotBugsRunner runner) {
    var classFile = Path.of("target/test-classes/com/sciencesakura/example/test/Hoge.class");
    var bugs = runner.performAnalysis(customization, classFile).getCollection();
    bugs.forEach(b -> {
      System.err.println(b.getBugPattern().getType() + ' ' + b.getMessage() + ' ' + b.getPrimarySourceLineAnnotation());
    });
    assertEquals(1, bugs.size());
    var bug = bugs.toArray(new BugInstance[0])[0];
    assertEquals("X_DISALLOW_HOGE", bug.getBugPattern().getType());
    assertEquals("com.sciencesakura.example.test.Hoge", bug.getPrimaryClass().getClassName());
  }

  @Test
  @DisplayName("OKケース")
  void ok(SpotBugsRunner runner) {
    var classFile = Path.of("target/test-classes/com/sciencesakura/example/test/Fuga.class");
    var bugs = runner.performAnalysis(customization, classFile).getCollection();
    bugs.forEach(b -> {
      System.err.println(b.getBugPattern().getType() + ' ' + b.getMessage() + ' ' + b.getPrimarySourceLineAnnotation());
    });
    assertEquals(0, bugs.size());
  }
}
