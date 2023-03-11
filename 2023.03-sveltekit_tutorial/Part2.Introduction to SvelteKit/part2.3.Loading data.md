# Loading data

## [Page data](https://learn.svelte.dev/tutorial/page-data)

* SvelteKit의 작업은 세가지로 요약됩니다.

    1) Routing : 들어오는 경로와 일치하는 경로 파악

    2) Loading : 경로에 필요한 데이터 가져오기

    3) Rendering : 일부 HTML 생성(서버에서) 또는 DOM 업데이트(브라우져)

* 라우팅과 렌더링은 앞에서 학습했고, 이제 로딩에 대해서 이야기 합니다.

* 앱의 모든 페이지에서 `+page.server.js`에서 `load` function을 선언할수 있습니다.

* 파일이름에서 추측할수 있듯이, 이 모듈은 클라이언트 네비게이션을 포함하여 모두 서버에서 실행됩니다.

* 아래는 종합적인 예제이다.

src/routes/data.js
```js
export const posts = [
	{
		slug: 'welcome',
		title: 'Welcome to the Aperture Science computer-aided enrichment center',
		content:
			'<p>We hope your brief detention in the relaxation vault has been a pleasant one.</p><p>Your specimen has been processed and we are now ready to begin the test proper.</p>'
	},

	{
		slug: 'safety',
		title: 'Safety notice',
		content:
			'<p>While safety is one of many Enrichment Center Goals, the Aperture Science High Energy Pellet, seen to the left of the chamber, can and has caused permanent disabilities, such as vaporization. Please be careful.</p>'
	},

	{
		slug: 'cake',
		title: 'This was a triumph',
		content: "<p>I'm making a note here: HUGE SUCCESS.</p>"
	}
];
```

src/routes/blog/+page.server.js
```js
import { posts } from './data.js';

export function load() {
  return {
    summaries: posts.map((post) => ({
      slug: post.slug,
      title: post.title
    }))
  }
}
```

src/routes/blog/+page.svelte
```js
<script>
	export let data;
</script>

<h1>blog</h1>

<ul>
	{#each data.summaries as { slug, title }}
	  <li><a href="/blog/{slug}">{title}</a></li>
	{/each}
</ul>
```

src/routes/blog/[slug]/+page.server.js
```js
import { error } from '@sveltejs/kit'
import { posts } from '../data.js'

export function load({ params }) {
  const post = posts.find((post) => post.slug == params.slug)

  if (!post) throw error(404)

  return {
    post
  }
}
```

src/routes/blog/[slug]/+page.svelte
```js
<script>
  export let data;
</script>

<h1>{data.post.title}</h1>
<div>{@html data.post.content}</div>
```

## [Layout data](https://learn.svelte.dev/tutorial/layout-data)

* `+layout.svelte` 파일이 모든 하위경로에 적용되듯이, `+layout.server.js` 파일도 모든 하위 경로에 적용됩니다.

* 앞의 예제에서 data를 불러오는 부분은 하위에서도 반복적으로 필요하다. (물론 data.js에서 읽어오기는 하지만, 실제로는 서버에 요청하는 경우가 많을것이다.)

* 이 예제에서는 `src/routes/blog/+page.server.js`를  `src/routes/blog/+layout.server.js`로 파일명을 변경하고, 이후 `data.summaries`를 사용하는 에제를 보여준다.

* 추가로, 하위 페이지인 `src/routes/blog/[slug]/+layout.svelte` 파일에서 더보기와 같은 layout ui를 적용한다.

* 최종적으로 적용된 layout 스타일은 root의 layout을 적용받고, [slug]/+layout.svelte layout을 적용받는다.

src/routes/blog/[slug]/+layout.svelte
```js
<script>
	export let data;
</script>

<div class="layout">
	<main>
		<slot />
	</main>

	<aside>
		<h2>More posts</h2>
		<ul>
			{#each data.summaries as { slug, title }}
				<li>
					<a href="/blog/{slug}">{title}</a>
				</li>
			{/each}
		</ul>
	</aside>
</div>

<style>
	@media (min-width: 640px) {
		.layout {
			display: grid;
			gap: 2em;
			grid-template-columns: 1fr 16em;
		}
	}
</style>
```

## Universal load functions

* refer to [document](https://kit.svelte.dev/docs/load#shared-vs-server)