# Events

## [DOM events](https://learn.svelte.dev/tutorial/dom-events)

* `on` 지시어를 사용하여 이벤트를 핸들링 할수 있다.


```js
<script>
	let m = { x: 0, y: 0 };

	function handleMousemove(event) {
		m.x = event.clientX;
		m.y = event.clientY;
	}
</script>

<div on:mouseover={handleMousemove}>
	The mouse position is {m.x} x {m.y}
</div>

<style>
	div {
		width: 100%;
		height: 100%;
	}
</style>
```

