MFileChooser
======
A simple and nice file chooser

#### Screenshot
<img src="gif/MFileChooser.gif"/>


### Download
First, Add it in your root build.gradle at the end of repositories:
```groovy
allprojects {
    repositories {
        jcenter()
        maven { url "https://jitpack.io" }
    }
}
```
Then,Add the dependency:
```groovy
compile 'com.github.Even201314:MFileChooser:1.0.0'
```

## Usage

```groovy
    FileChooser.Builder builder =
        new FileChooser.Builder(this).setChooserMode(FileChooser.MODE_FILE)
            .setOnChooserSelectedListener(new OnChooserSelectedListener() {
                @Override public void onSelect(String path) {
                    Log.d(MainActivity.class.getSimpleName(), path);
                }
            });
    builder.build().show(getFragmentManager(), MainActivity.class.getSimpleName());
```

## License
```
The MIT License (MIT)

Copyright © 2017 Even201314,

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
```
