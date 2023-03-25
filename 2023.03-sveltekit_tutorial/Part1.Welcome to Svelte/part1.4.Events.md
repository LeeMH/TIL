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

## [inline handlers](https://learn.svelte.dev/tutorial/inline-handlers)

* 코드 블럭이 직접 핸들러를 구현할수도 있다.

* 일부 프레임워크에서는 성능상의 문제로 권장되지 않지만, svelte에서는 어떤 방식을 사용해도 상관없음을 강조.

```js
<script>
	let m = { x: 0, y: 0 };

	function handleMousemove(event) {
		m.x = event.clientX;
		m.y = event.clientY;
	}
</script>

<div on:mousemove={e => m = {x: e.clientX, y: e.clientY}}>
	The mouse position is {m.x} x {m.y}
</div>

<style>
	div {
		width: 100%;
		height: 100%;
	}
</style>
```

## [Event modifiers](https://learn.svelte.dev/tutorial/event-modifiers)

* DOM 이벤트 핸들러는 행동을 변경하는 `modifier`를 가질수 있습니다.

* 예제에서 `click|once` 가 함께 지정되어 해당 이벤트는 한번만 수행된다.

* 또한 modifier는 여러개를 연결해서 사용할수도 있습니다. 

    * 예) `on:click|once|captrue={...}`

```js
<script>
	function handleClick() {
		alert('clicked');
	}
</script>

<button on:click|once={handleClick}> Click me </button>
```

* modifier 리스트

    * `preventDefault`

	* `stopPropagation`

	* `passive`

	* `nonpassive`

	* `capture`

	* `once`

	* `self`

	* `trusted`


## [Event forwarding](https://learn.svelte.dev/tutorial/event-forwarding)

* DOM 이벤트와 달리, 컴포넌트 이벤트는 버블링 되지 않습니다.

* 따라서, 컴포넌트가 중첩된 구조라면 중간에 있는 컴포넌트가 이벤트를 전달해 주어야 합니다.

* 방법은 `createEventDispatcher`를 추가하고 전달해야 한다.

* 아래 예시에서 한번더 설명을 해주지만, 코드양이 상당하다. 이경우 `on:message` 상용구를 사용하면 편하다. 값이 없는 지시문은 모두 이벤트 전달을 의미한다.


App.svelte
```js
<script>
	import Outer from './Outer.svelte';

	function handleMessage(event) {
		alert(event.detail.text);
	}
</script>

<Outer on:message={handleMessage} />
```


Outer.svelte 
```js
<script>
	import Inner from './Inner.svelte';
	import { createEventDispatcher } from 'svelte';

	const dispatch = createEventDispatcher();

	function forward(event) {
		dispatch('message', event.detail)
	}
</script>

<Inner on:message={forward}/>
```

Outer.svelte (상용구 사용버전)
```js
<script>
	import Inner from './Inner.svelte';
</script>

<Inner on:message/>
```


Inner.svelte
```js
<script>
	import { createEventDispatcher } from 'svelte';

	const dispatch = createEventDispatcher();

	function sayHello() {
		dispatch('message', {
			text: 'Hello!'
		});
	}
</script>

<button on:click={sayHello}>
	Click to say hello
</button>
```


## [DOM event forwarding](https://learn.svelte.dev/tutorial/dom-event-forwarding)

* 이벤트 포워딩은 DOM 이벤트에도 적용됩니다.

* 동일하게 하위 컴포넌트에서 값이 없는 이벤트를 선언해주면 된다.

App.svelte
```js
<script>
	import CustomButton from './CustomButton.svelte';

	function handleClick() {
		alert('Button Clicked');
	}
</script>

<CustomButton on:click={handleClick} />
```

CustomButton.svelte
```js
<button on:click>Click me</button>

<style>
	button {
		background: #e2e8f0;
		color: #64748b;
		border: unset;
		border-radius: 6px;
		padding: 0.75rem 1.5rem;
		cursor: pointer;
	}

	button:hover {
		background: #cbd5e1;
		color: #475569;
	}

	button:focus {
		background: #94a3b8;
		color: #f1f5f9;
	}
</style>
```