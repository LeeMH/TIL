# Lifecycle

## [onMount](https://learn.svelte.dev/tutorial/onmount)

* 모든 컴포넌트는 라이프사이클을 가지고 있습니다.

* 가장 자주 사용하게 될것은 `onMount` 입니다. 이 이벤트는 DOM이 렌더링되고 호출됩니다.

```js
<script>
	import { onMount } from 'svelte'
	let photos = [];

	onMount(async() => {
		const res = await fetch(`https://jsonplaceholder.typicode.com/photos?_limit=20`)
		photos = await res.json()
	})
</script>

<h1>Photo album</h1>

<div class="photos">
	{#each photos as photo}
		<figure>
			<img
				src={photo.thumbnailUrl}
				alt={photo.title}
			/>
			<figcaption>{photo.title}</figcaption>
		</figure>
	{:else}
		<!-- this block renders when photos.length === 0 -->
		<p>loading...</p>
	{/each}
</div>

<style>
	.photos {
		width: 100%;
		display: grid;
		grid-template-columns: repeat(5, 1fr);
		grid-gap: 8px;
	}

	figure,
	img {
		width: 100%;
		margin: 0;
	}
</style>
```

## [onMount](https://learn.svelte.dev/tutorial/onmount)

* 콤포넌트가 소멸될때 호출하려면 `onDestroy`를 사용합니다.

```js
```
