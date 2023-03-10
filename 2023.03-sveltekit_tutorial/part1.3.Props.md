# Props

## [Declaring props](https://learn.svelte.dev/tutorial/declaring-props)

* 여기까지 내부상태 변경에 대해서만 알아보았습니다.

* 이제 한 구성요소에서 하위 항목으로 데이터를 전송하는 방법 `props`에 대해 간단한 예제를 보겠습니다.

* `export`라는 키워드를 사용해 간단하게 property를 선언할수 있다.

App.svelte
```js
<script>
	import Nested from './Nested.svelte';
</script>

<Nested answer={42} />
```

Nested.svelte
```js
<script>
	export let answer;
</script>

<p>The answer is {answer}</p>
```

## [Default values](https://learn.svelte.dev/tutorial/default-values)

* 변수에 값 할당으로 손쉽게 디폴트 값을 선언할수 있다.

```js
<script>
	export let answer = 'a mystery';
</script>

<p>The answer is {answer}</p>
```

## [Spread props](https://learn.svelte.dev/tutorial/spread-props)

* 바인딩 되는 변수가 여러개이고 소스가 오브젝트라면 spread 연산자를 통해 쉽게 매핑할수 있다.

* 매핑은 순서가 아닌, 이름으로 매핑된다. (hello: 'world' 참조)

App.svelte
```js
<script>
	import PackageInfo from './PackageInfo.svelte';

	const pkg = {
		hello: 'world',		
		name: 'svelte',
		version: 3,
		speed: 'blazingly',
		website: 'https://svelte.dev'
	};
</script>

<PackageInfo {...pkg}/>
```

Packageinfo.svelte
```js
<script>
	export let name;
	export let version;
	export let speed;
	export let website;

	$: href = `https://www.npmjs.com/package/${name}`;
</script>

<p>
	The <code>{name}</code> package is {speed} fast. Download version {version}
	from
	<a {href}>npm</a>
	and <a href={website}>learn more here</a>
</p>

<style>
	code {
		font-family: Menlo;
		padding: 0.1em 0.2em;
		border-radius: 0.2em;
	}
</style>
```

## [If blocks](https://learn.svelte.dev/tutorial/if-blocks)

* HTML에는 논리 분기로직이 없지만, svelte에서는 if 구문을 통해 쉽게 논리적 분기가 가능하다.

```js
<script>
	let user = { loggedIn: false };

	function toggle() {
		user.loggedIn = !user.loggedIn;
	}
</script>
{#if user.loggedIn}
  <button on:click={toggle}>
	  Log out
  </button>
{/if}

{#if !user.loggedIn}
  <button on:click={toggle}>
	  Log in
  </button>
{/if}
```

## [Else blocks](https://learn.svelte.dev/tutorial/else-blocks)

* else 구분을 이용해, 직전 예제를 간단히 나타낼수 있음을 설명한다.

```js
<script>
	let user = { loggedIn: false };

	function toggle() {
		user.loggedIn = !user.loggedIn;
	}
</script>

{#if user.loggedIn}
	<button on:click={toggle}>
		Log out
	</button>
{/if}

{#if !user.loggedIn}
	<button on:click={toggle}>
		Log in
	</button>
{/if}
```

## [Else-if blocks](https://learn.svelte.dev/tutorial/else-if-blocks)

* else-if 블럭에 대해서 설명한다.

```js
<script>
	let x = 7;
</script>

{#if x > 10}
	<p>{x} is greater than 10</p>
{:else if 5 > x}
		<p>{x} is less than 5</p>
{:else}
		<p>{x} is between 5 and 10</p>
{/if}
```

## [Each blocks](https://learn.svelte.dev/tutorial/each-blocks)

* 반복이 필요할때는 each 블럭을 사용한다.

* index가 필요한 경우, `{#each 컬렉션 as fetch변수, index변수}`형태로 사용한다.

```js
<script>
	let cats = [
		{
			id: 'J---aiyznGQ',
			name: 'Keyboard Cat'
		},
		{
			id: 'z_AbfPXTKms',
			name: 'Maru'
		},
		{
			id: 'OUtn3pvWmpg',
			name: 'Henri The Existential Cat'
		}
	];
</script>

<h1>The Famous Cats of YouTube</h1>

<ul>
	{#each cats as cat, i}
	<li>
		<a
			target="_blank"
			href="https://www.youtube.com/watch?v={cat.id}"
		>
			{i + 1} : {cat.name}
		</a>
	</li>
	{/each}
</ul>
```

## [Keyed each blocks](https://learn.svelte.dev/tutorial/keyed-each-blocks)

* 개인적으로 이해하기 어려운 파트이다.

* 현상은 알겠지만 이유는 이해가 잘 되지 않는다.

* 다만, each 블럭에서 컬렉션을 사용하는 경우 `keyed each`를 사용하도록 하자.

App.svelte
```js
<script>
	import Thing from './Thing.svelte';

	let things = [
		{ id: 1, name: 'apple' },
		{ id: 2, name: 'banana' },
		{ id: 3, name: 'carrot' },
		{ id: 4, name: 'doughnut' },
		{ id: 5, name: 'egg' }
	];

	function handleClick() {
		things = things.slice(1);
	}
</script>

<button on:click={handleClick}>
	Remove first thing
</button>

{#each things as thing (thing.id)}
	<Thing name={thing.name} />
{/each}
```

Thing.svelte
```js
<script>
	import { onDestroy } from 'svelte';

	const emojis = {
		apple: '🍎',
		banana: '🍌',
		carrot: '🥕',
		doughnut: '🍩',
		egg: '🥚'
	};

	// the name is updated whenever the prop value changes...
	export let name;

	// ...but the "emoji" variable is fixed upon initialisation of the component
	const emoji = emojis[name];

	// observe in the console which entry is removed
	onDestroy(() => {
		console.log('thing destroyed: ' + name);
	});
</script>

<p>
	<span>The emoji for {name} is {emoji}</span>
</p>

<style>
	p {
		margin: 0.8em 0;
	}

	span {
		display: inline-block;
		padding: 0.2em 1em 0.3em;
		text-align: center;
		border-radius: 0.2em;
		background-color: #ffdfd3;
	}

	@media (prefers-color-scheme: dark) {
		span {
			background-color: #4f352b;
		}
	}
</style>
```

## [Await blocks](https://learn.svelte.dev/tutorial/await-blocks)

* await 블럭을 이용하면 promise를 쉽게 처리할수 있다.

* 가장 최근의 promise만 고려되기때문에 경쟁(race condition)을 걱정하지 않아도 된다고 한다.

```js
<script>
	async function getRandomNumber() {
		const res = await fetch(
			`https://svelte.dev/tutorial/random-number`
		);
		const text = await res.text();

		if (res.ok) {
			return text;
		} else {
			throw new Error(text);
		}
	}

	let promise = getRandomNumber();

	function handleClick() {
		promise = getRandomNumber();
	}
</script>

<button on:click={handleClick}> generate random number </button>

{#await promise}
  <p>{promise}</p>
{:then number}
  <p>the numer is {number}</p>
{:catch error}
  <p sytle="color: red">{error.message}</p>
{/await}
```