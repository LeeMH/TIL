# Routing

## [Pages](https://learn.svelte.dev/tutorial/pages)

* SvelteKit은 파일 베이스 라우팅을 합니다. 이말은 파일이 어느 디렉토리에 위치했는지에 의해 열리는 페이지가 달라질수 있습니다.

* 경로는 src/routes 폴더에 있고, 모든 디렉토리는 `+page.svelte`파일을 라우팅을 위해 포함 합니다.

* 아래는 /, /about이 각각 로딩되는 예제입니다.

src/routes/+page.svelte
```js
<nav>
	<a href="/">home</a>
	<a href="/about">about</a>
</nav>

<h1>home</h1>
<p>this is the home page.</p>
```

src/routes/about/+page.svelte
```js
<nav>
  <a href="/">home</a>
  <a href="/about">about</a>
</nav>

<h1>about</h1>
<p>this is about page.</p>
```

## [Layouts](https://learn.svelte.dev/tutorial/layouts)

* 라우팅 경로는 다르지만, 공통 ui를 공유해야 하는 경우가 있습니다.

* 이런경우, `+layout.svelte`를 사용하면 쉽게 적용할수 있습니다.

* `+layout.svelte`는 같은 경로는 물론 하위 경로까지 모두 적용됩니다.

src/routes/+layout.svelte
```js
<nav>
  <a href="/">home</a>
  <a href="/about">about</a>
</nav>

<slot/>
```

## [Route parameters](https://learn.svelte.dev/tutorial/params)

* url에서 동적으로 변수화 하려면, 대괄호로 변수를 묶으면 됩니다.

* `src/routes/blog/[slug]/+page.svelte`는 `/blog/one`, `/blog/two`, `/blog/three`등과 매칭 됩니다.