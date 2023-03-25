# Introduction

## [Welcome to  Svelte](https://learn.svelte.dev/tutorial/welcome-to-svelte)

### What is Svelte?

* Svelte는 마크업, 스타일 및 동작을 선언적으로 컴포넌트화하고 빌드할수 있다고 한다.

* 이러한 컴포넌트들은 작고 효율적인 javascript로 `컴파일`되고, 전통적인 UI 프레임워크에서 오버헤드를 제거한다고 설명한다.

## [Your first component](https://learn.svelte.dev/tutorial/your-first-component)

* 간단하게 data 를 추가하고 html에 바인딩하는 예제를 보여준다.

```js
<script>
  let name = 'Svelte'
</script>

<h1>Hello {name.toUpperCase()}!</h1>
```

## [Dynamic attributes](https://learn.svelte.dev/tutorial/dynamic-attributes)

* 속성에 변수를 바인딩 하는 방법을 간단하게 보여준다.

* 변수명과 속성이 동일하다면 `<img {src} ../>`  이런식으로 축약표현도 할수 있다.

```js
<script>
	let src = '/image.gif';
</script>

<img src={src} alt="A man dances."/>
```

## [Styling](https://learn.svelte.dev/tutorial/styling)

* css 스타일 적용 예시를 보여준다.

* 중요한것은 범위가 해당 컴포넌트에만 제한단다는 것을 알려준다.

```js
<p>This is a paragraph.</p>

<style>
	p {
      color: purple;
      font-family: 'Comic Sans MS', cursive;
      font-size: 2em;
  }
</style>
```

## [Nested components](https://learn.svelte.dev/tutorial/nested-components)

* 컴포넌트를 불러 오는 방법에 대해서 설명한다.

* 스타일링에서 설명한 css가 해당 컴포넌트에만 적용된다는것을 다시한번 설명한다.

* App.svelte -> Nested.svelte를 호출해서 사용하고 있으나, p 태그에 대한 스타일링은 App.svelte에 제한된다.

App.svelte
```js
<script>
  import Nested from './Nested.svelte'
</script>

<p>This is a paragraph.</p>
<Nested/>

<style>
	p {
		color: purple;
		font-family: 'Comic Sans MS', cursive;
		font-size: 2em;
	}
</style>
```

Nested.svelte
```js
<p>This is another paragraph.</p>
```
