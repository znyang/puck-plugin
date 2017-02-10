# Puck

<img alt="Puck is an android library" src="https://www.cleveroad.com/public/comercial/label-android.svg" height="20">
[![Build Status](https://travis-ci.org/znyang/puck-plugin.svg?branch=master)](https://travis-ci.org/znyang/puck-plugin)
[![](https://jitpack.io/v/znyang/puck-plugin.svg)](https://jitpack.io/#znyang/puck-plugin)
[![codecov](https://codecov.io/gh/znyang/puck-plugin/branch/master/graph/badge.svg)](https://codecov.io/gh/znyang/puck-plugin)

![logo](/img/logo.jpg)

## Gradle配置

[JitPack](https://jitpack.io/#znyang/puck-plugin)

```gradle
buildscript {
    repositories {
        maven { url "https://jitpack.io" }
        // ...
    }
    dependencies {
        classpath 'com.android.tools.build:gradle:2.2.2'
        classpath 'com.github.znyang:puck-plugin:0.1-beta8'
        // ...
    }
}
```

```gradle

// android module build.gradle
apply plugin: 'com.zen.android.puck'

```

## Dependencies

* Robolectric
* Mockito
* rxjava
* rxandroid

## 特性

### Robolectric

* [解决AndroidManifest.xml文件找不到的问题](/doc/android-manifest-not-found.md)
* 解决当前项目的依赖库中的assets文件找不到的问题(copyAssetsTask)

### RxJava & RxAndroid

* 替换调度器源，解决测试过程中异步无回调问题（异步转同步）

### Jacoco & Sonar

* 建立jacoco Task和sonarqube/sonarRuner Task之间的关联

## 用法

使用 `PuckTestRunner` 代替 `RobolectricTestRunner`

```java
@RunWith(PuckTestRunner.class)
@Config(constants = BuildConfig.class)
public class MainActivityTest {
	// test methods
}
```
