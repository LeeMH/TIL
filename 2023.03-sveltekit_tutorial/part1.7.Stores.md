# Stores

## [Writable stores](https://learn.svelte.dev/tutorial/writable-stores)

* 애플리케이션 상태가 컴포넌트의 구조에만 종속되는건 아니다.

* 때로는 상관없는 여러 컴포넌트에서 특정값에 접근해야 하는 경우도 있다.

* svelte에서는 store로 할수 있다. store는 특정 값이 변경되면  `subscribe` 메소드에 통지할수 있는 간단한 오브젝트 이다.

* 아래 예시에서 `count`는 `store`이고, `count_value`는 `count의 콜백`이다.


App.svelte
```js
<script>
	import { count } from './stores.js';
	import Incrementer from './Incrementer.svelte';
	import Decrementer from './Decrementer.svelte';
	import Resetter from './Resetter.svelte';

	let count_value;

	count.subscribe((value) => {
		count_value = value;
	});
</script>

<h1>The count is {count_value}</h1>

<Incrementer />
<Decrementer />
<Resetter />
```

Decrementer.svelte
```js
<script>
	import { count } from './stores.js';

	function decrement() {
		count.update((n) => n -1)		
	}
</script>

<button on:click={decrement}> - </button>
```

Incrementer.svelte
```js
<script>
	import { count } from './stores.js';

	function increment() {
		count.update((n) => n + 1)
	}
</script>

<button on:click={increment}> + </button>
```

Resetter.svelte
```js
<script>
	import { count } from './stores.js';

	function reset() {
		count.set(0)
	}
</script>

<button on:click={reset}> reset </button>
```

stores.js
```js
import { writable } from 'svelte/store';

export const count = writable(0);
```

## Auto-subscription

* 앞의 예제는 구독해제가 안되는 버그가 있습니다.

* 이를 해결하기 위해서는 아래와 같이 컴포넌트 종료시 구축해제가 되어야 합니다.

```js
<script>
	import { onDestroy } from 'svelte';
	import { count } from './stores.js';
	import Incrementer from './Incrementer.svelte';
	import Decrementer from './Decrementer.svelte';
	import Resetter from './Resetter.svelte';

	let count_value;

	const unsubscribe = count.subscribe((value) => {
		count_value = value;
	});

	onDestroy(unsubscribe);
</script>

<h1>The count is {count_value}</h1>

<Incrementer />
<Decrementer />
<Resetter />
```

* 하지만 컴포넌틑가 많아지면 중복되는 작업이 많아 집니다.

* 그래서 `$` 지시어를 통해 store 접근할수 있는 방법을 제공 합니다.

* 이 방법은 top-level scope에서 선언되었거나 import된 경우에만 동작 합니다.

```js
<script>
	import { count } from './stores.js';
	import Incrementer from './Incrementer.svelte';
	import Decrementer from './Decrementer.svelte';
	import Resetter from './Resetter.svelte';
</script>

<h1>The count is {$count}</h1>

<Incrementer />
<Decrementer />
<Resetter />
```

## [Readable stores](https://learn.svelte.dev/tutorial/readable-stores)

* 모든 store에 쓰기가 필요한것은 아닙니다.

* 읽기만 필요한 경우, readable 속성의 store를 만들수 있습니다.

* 아래 예시에서, readable 메소드의 첫번째 인자는 초기값 입니다.

* 다음 인자는 시작시 호출되는 콜백메소드이고, 리턴하는 메소드는 종료시 호출되는 메소드 입니다.

App.svelte
```js
<script>
	import { time } from './stores.js';

	const formatter = new Intl.DateTimeFormat(
		'en',
		{
			hour12: true,
			hour: 'numeric',
			minute: '2-digit',
			second: '2-digit'
		}
	);
</script>

<h1>The time is {formatter.format($time)}</h1>
```

stores.js
```js
import { readable } from 'svelte/store';

export const time = readable(null, function start(set) {
	const interval = setInterval(() => {
    set(new Date());
  }, 1000)

	return function stop() {
    clearInterval(interval)
  };
});
```


## [Derived stores]((https://learn.svelte.dev/tutorial/derived-stores)

* 다른 store 값에서 파생된 store도 `derived` 지시를 통해 만들수 있다.


App.svelte
```js
<script>
	import { time, elapsed } from './stores.js';

	const formatter = new Intl.DateTimeFormat(
		'en',
		{
			hour12: true,
			hour: 'numeric',
			minute: '2-digit',
			second: '2-digit'
		}
	);
</script>

<h1>The time is {formatter.format($time)}</h1>

<p>
	This page has been open for
	{$elapsed}
	{$elapsed === 1 ? 'second' : 'seconds'}
</p>
```

stores.js
```js
import { readable, derived } from 'svelte/store';

export const time = readable(new Date(), function start(set) {
	const interval = setInterval(() => {
		set(new Date());
	}, 1000);

	return function stop() {
		clearInterval(interval);
	};
});

const start = new Date();

export const elapsed = derived(time, ($time) => Math.round($time - start) / 1000);
```

## [Custom stores](https://learn.svelte.dev/tutorial/custom-stores)

* 도메인 로직을 결합하여 커스텀 store를 만드는것도 간단합니다.

App.svelte
```js
<script>
	import { count } from './stores.js';
</script>

<h1>The count is {$count}</h1>

<button on:click={count.increment}>+</button>
<button on:click={count.decrement}>-</button>
<button on:click={count.reset}>reset</button>
```

stores.js
```js
import { writable } from 'svelte/store';

function createCount() {
	const { subscribe, set, update } = writable(0);

	return {
		subscribe,
		increment: () => update((n) => n+1),
		decrement: () => update((n) => n-1),
		reset: () => set(0)
	};
}

export const count = createCount();
```


## [Store binding](https://learn.svelte.dev/tutorial/store-bindings)

* store를 컴포넌트에 바인딩하는것도 쉽게 가능합니다.

* `<input bind:value={$name}/>` 이런식으로 바인딩 할수 있습니다.

* 아래는 버튼을 클릭하면 !를 store에 하니씩 붙이는 에제이다.

* name store가 변경되면, 관련있는 부분은 업데이트가 이뤄진다.

* `$name += '!'` 과 `name.set($name + '!')`은 같은 로직이다.

App.svelte
```js
<script>
	import { name, greeting } from './stores.js';
</script>

<h1>{$greeting}</h1>
<input value={$name} />

<button on:click={() => $name += '!'}>
	Add exclamation mark!!
</button>
```

stores.js
```js
import { writable, derived } from 'svelte/store';

export const name = writable('world');

export const greeting = derived(name, ($name) => `Hello ${$name}!`);
```
