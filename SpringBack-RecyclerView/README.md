# Spring Back RecyclerView

A sample to show how we can implement a spring back functionality for Recyclerview.

When you fling the `RecyclerView` & it reaches either end or to the top of the list the remaining momentum will be dispersed to create this (below gif) _Spring_ like animation. Check the [sample app](/art) from the releases to see it in action.

<img height="420px" src="art/demo.gif"/>

_Gif is Playing at 0.5x_

## Usage

This is not a library but a sample build for an article that I wrote on Medium (see **Resources**). TBH, this is just my take on how the animation should have been implemented. Feel free to modify the code in any way you want.

- Add [this](https://developer.android.com/jetpack/androidx/releases/dynamicanimation#declaring_dependencies) dependency & copy the [SpringScrollHelper](https://github.com/KaustubhPatange/spring-back-recyclerview/blob/master/app/src/main/java/com/kpstv/dampingrecyclerview/SpringScrollHelper.kt) class to your project.

```kotlin
SpringScrollHelper().attachToRecyclerView(recyclerView)
```

## Resources

- [Spring Back Recyclerview - The basics](https://kaustubhpatange.medium.com/spring-back-recyclerview-the-basics-beebe3477cad) - An article explaining how to create this animation.

## License

- [The Apache License Version 2.0](https://www.apache.org/licenses/LICENSE-2.0.txt)

```
Copyright 2021 Kaustubh Patange

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   https://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
