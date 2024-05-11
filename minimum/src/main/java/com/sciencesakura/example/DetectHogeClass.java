package com.sciencesakura.example;

import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.Detector;
import edu.umd.cs.findbugs.ba.ClassContext;

/**
 * {@code Detector} 実装例: クラス名に `Hoge` を含むクラスを検出する.
 */
public class DetectHogeClass implements Detector {

  private final BugReporter reporter;

  /**
   * ① {@code BugReporter} 型の引数を受け取るコンストラクタを実装しないといけない.
   */
  public DetectHogeClass(BugReporter reporter) {
    this.reporter = reporter;
  }

  /**
   * ②クラスファイルを解析するメソッド.
   */
  @Override
  public void visitClassContext(ClassContext classContext) {
    var className = classContext.getClassDescriptor().getSimpleName();
    if (className.contains("Hoge")) {
      // ③バグを報告する
      reporter.reportBug(new BugInstance(this, "X_DISALLOW_HOGE", NORMAL_PRIORITY)
          .addClass(classContext.getClassDescriptor()));
    }
  }

  /**
   * ④すべてのクラスファイルの解析が完了したあとに呼ばれるメソッド.
   */
  @Override
  public void report() {
  }
}
