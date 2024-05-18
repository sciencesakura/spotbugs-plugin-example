package com.sciencesakura.example.test;

import org.springframework.stereotype.Component;

@Component
public class MutableSpringBean extends BaseClass {

  public static int static_field_1 = 0;

  public final int field_1 = 0;
  public int field_2 = 0;                       // NG: 非privateかつ非finalなインスタンスフィールド
  private int field_3 = 0;

  private static final OtherClass static_other = new OtherClass();
  private final OtherClass other = new OtherClass();

  static {
    var bean = new MutableSpringBean();
    // 自クラスのフィールド代入
    bean.field_3++;
    // 親クラスのフィールド代入
    bean.super_field_1++;

    // 自クラスのフィールドのメソッド呼び出し
    bean.other.setValue(static_field_1);
    // 親クラスのフィールドのメソッド呼び出し
    bean.super_other.setValue(static_field_1);
  }

  MutableSpringBean() {
    // 自クラスのフィールド代入
    field_3++;
    // 親クラスのフィールド代入
    super_field_1++;

    // 自クラスのフィールドのメソッド呼び出し
    other.setValue(static_field_1);
    // 親クラスのフィールドのメソッド呼び出し
    super_other.setValue(static_field_1);
  }

  static void method_1(MutableSpringBean bean) {
    // 自クラスのフィールド代入
    static_field_1++;
    bean.field_3++;                             // NG: インスタンスフィールドに値を代入している
    // 親クラスのフィールド代入
    bean.super_field_1++;                       // NG: インスタンスフィールドに値を代入している
    // 他クラスのフィールド代入
    bean.other.other_field_1++;

    // 自クラスのフィールドのメソッド呼び出し
    static_other.setValue(static_field_1);
    bean.other.setValue(static_field_1);        // NG: インスタンスフィールドに対して `set` で始まるインスタンスメソッドを実行している
    bean.other.getValue(static_field_1);
    // 親クラスのフィールドのメソッド呼び出し
    bean.super_other.setValue(static_field_1);  // NG: インスタンスフィールドに対して `set` で始まるインスタンスメソッドを実行している
    // 他クラスのフィールドのメソッド呼び出し
    bean.other.other_other.setValue(static_field_1);
  }

  void method_2() {
    // 自クラスのフィールド代入
    static_field_1++;
    field_3++;                                  // NG: インスタンスフィールドに値を代入している
    // 親クラスのフィールド代入
    super_field_1++;                            // NG: インスタンスフィールドに値を代入している
    // 他クラスのフィールド代入
    other.other_field_1++;

    // 自クラスのフィールドのメソッド呼び出し
    static_other.setValue(static_field_1);
    other.setValue(static_field_1);             // NG: インスタンスフィールドに対して `set` で始まるインスタンスメソッドを実行している
    other.getValue(static_field_1);
    // 親クラスのフィールドのメソッド呼び出し
    super_other.setValue(static_field_1);       // NG: インスタンスフィールドに対して `set` で始まるインスタンスメソッドを実行している
    // 他クラスのフィールドのメソッド呼び出し
    other.other_other.setValue(static_field_1);
  }
}
