package com.sciencesakura.example;

import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.Detector2;
import edu.umd.cs.findbugs.classfile.CheckedAnalysisException;
import edu.umd.cs.findbugs.classfile.ClassDescriptor;

/**
 * {@code Detector2} 実装例: クラス名に `Hoge` を含むクラスを検出する.
 */
public class DetectHogeClass2 implements Detector2 {

  private final BugReporter reporter;

  public DetectHogeClass2(BugReporter reporter) {
    this.reporter = reporter;
  }

  /**
   * {@code Detector#visitClassContext(ClassContext)} に相当するメソッド.
   */
  @Override
  public void visitClass(ClassDescriptor classDescriptor) {
    var className = classDescriptor.getSimpleName();
    if (className.contains("Hoge")) {
      reporter.reportBug(new BugInstance(this, "X_DISALLOW_HOGE", NORMAL_PRIORITY)
          .addClass(classDescriptor));
    }
  }

  /**
   * {@code Detector#report()} に相当するメソッド.
   */
  @Override
  public void finishPass() {
  }

  @Override
  public String getDetectorClassName() {
    return getClass().getName();
  }
}
