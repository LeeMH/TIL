# Bindings

## [Text inputs](https://learn.svelte.dev/tutorial/text-inputs)

* 일반적으로 svelte에서 데이터 흐름은 top down 방식 입니다.

* 하지만, text input등에서는 데이터가 역으로도 흘러야 합니다.

* `bind:value` 지시어로 간단히 사용할수 있습니다.



```js
<script>
	let name = 'world111';
</script>

<input bind:value={name} />

<h1>Hello {name}!</h1>
```

## [Numeric inputs](https://learn.svelte.dev/tutorial/numeric-inputs)

* DOM에서는 모든 데이터가 문자열 입니다.

* 하지만 `type="number"`, `type="range"` 속성을 사용하면, 숫자형 데이터로 사용할수 있도록 해줍니다.

```js
<script>
	let a = 1;
	let b = 2;
</script>

<label>
	<input
		type="number"
		bind:value={a}
		min="0"
		max="10"
	/>

	<input
		type="range"
		bind:value={a}
		min="0"
		max="10"
	/>
</label>

<label>
	<input
		type="number"
		bind:value={b}
		min="0"
		max="10"
	/>

	<input
		type="range"
		bind:value={b}
		min="0"
		max="10"
	/>
</label>

<p>{a} + {b} = {a + b}</p>

<style>
	label {
		display: flex;
	}

	input,
	p {
		margin: 6px;
	}
</style>
```

## [Checkbox inputs](https://learn.svelte.dev/tutorial/checkbox-inputs)

* 체크박스의 경우, `bind:checked={변수}` 형태로 사용합니다.

```js
<script>
	let yes = false;
</script>

<label>
	<input type="checkbox" bind:checked={yes} />
	Yes! Send me regular email spam
</label>

{#if yes}
	<p>
		Thank you. We will bombard your inbox and sell
		your personal details.
	</p>
{:else}
	<p>
		You must opt in to continue. If you're not
		paying, you're the product.
	</p>
{/if}

<button disabled={!yes}>Subscribe</button>
```

## [Group inputs](https://learn.svelte.dev/tutorial/group-inputs)

* 값을 그룹으로 묶으려면, `bind:group` 지시어를 사용한다.

```js
<script>
	let scoops = 1;
	let flavours = ['Mint choc chip'];

	function join(flavours) {
		if (flavours.length === 1) return flavours[0];
		return `${flavours.slice(0, -1).join(', ')} and ${flavours[flavours.length - 1]}`;
	}
</script>

<h2>Size</h2>

<label>
	<input type=radio bind:group={scoops} name="scoops" value={1}>
	One scoop
</label>

<label>
	<input type=radio bind:group={scoops} name="scoops" value={2}>
	Two scoops
</label>

<label>
	<input type=radio bind:group={scoops} name="scoops" value={3}>
	Three scoops
</label>

<h2>Flavours</h2>

<label>
	<input type=checkbox bind:group={flavours} name="flavours" value="Cookies and cream">
	Cookies and cream
</label>

<label>
	<input type=checkbox bind:group={flavours} name="flavours" value="Mint choc chip">
	Mint choc chip
</label>

<label>
	<input type=checkbox bind:group={flavours} name="flavours" value="Raspberry ripple">
	Raspberry ripple
</label>

{#if flavours.length === 0}
	<p>Please select at least one flavour</p>
{:else if flavours.length > scoops}
	<p>Can't order more flavours than scoops!</p>
{:else}
	<p>
		You ordered {scoops} {scoops === 1 ? 'scoop' : 'scoops'}
		of {join(flavours)}
	</p>
{/if}
```

## [Textarea inputs](https://learn.svelte.dev/tutorial/textarea-inputs)

* textarea도 text input 과 동일하게 `bind:value={변수명}`를 사용한다.

* 변수명도 동일하게 value라면 `bind=value` 축약 형태로 표현 가능하다.

```js
<script>
	import { marked } from 'marked';
	let value = `Some words are *italic*, some are **bold**`;
</script>

{@html marked(value)}

<textarea bind:value />

<style>
	textarea {
		width: 100%;
		height: 200px;
	}
</style>
```

## [Select bindings](https://learn.svelte.dev/tutorial/textarea-inputs)

* select에 대한 바인딩도 `bind:value` 지시어를 통해 가능하다.

* 단, 아래 예시에서 select된 option의 값은 문자열이 아니라, object 이다.

```js
<script>
	let questions = [
		{
			id: 1,
			text: `Where did you go to school?`
		},
		{
			id: 2,
			text: `What is your mother's name?`
		},
		{
			id: 3,
			text: `What is another personal fact that an attacker could easily find with Google?`
		}
	];

	let selected;

	let answer = '';

	function handleSubmit() {
		alert(
			`answered question ${selected.id} (${selected.text}) with "${answer}"`
		);
	}
</script>

<h2>Insecurity questions</h2>

<form on:submit|preventDefault={handleSubmit}>
	<select
		bind:value={selected}
		on:change={() => (answer = '')}
	>
		{#each questions as question}
			<option value={question}>
				{question.text}
			</option>
		{/each}
	</select>

	<input bind:value={answer} />

	<button disabled={!answer} type="submit">
		Submit
	</button>
</form>

<p>
	selected question {selected
		? selected.id
		: '[waiting...]'}
</p>

<style>
	input {
		display: block;
		width: 500px;
		max-width: 100%;
	}
</style>
```

## [Select multiple](https://learn.svelte.dev/tutorial/multiple-select-bindings)

* `multiple` 지시어를 통해 복수개 선택가능한 select 가 가능하다.

* 앞에서 만든 아이스크림에제를 select multiple로 변경한 예제이다.

```js
<script>
	let scoops = 1;
	let flavours = ['Mint choc chip'];

	let menu = [
		'Cookies and cream',
		'Mint choc chip',
		'Raspberry ripple'
	];

	function join(flavours) {
		if (flavours.length === 1) return flavours[0];
		return `${flavours.slice(0, -1).join(', ')} and ${flavours[flavours.length - 1]}`;
	}
</script>

<h2>Size</h2>

<label>
	<input type=radio bind:group={scoops} value={1}>
	One scoop
</label>

<label>
	<input type=radio bind:group={scoops} value={2}>
	Two scoops
</label>

<label>
	<input type=radio bind:group={scoops} value={3}>
	Three scoops
</label>

<h2>Flavours</h2>

<select multiple bind:value={flavours}>
	{#each menu as flavour}
	  <option value={flavour}>
			{flavour}
		</option>
{/each}

</select>

{#if flavours.length === 0}
	<p>Please select at least one flavour</p>
{:else if flavours.length > scoops}
	<p>Can't order more flavours than scoops!</p>
{:else}
	<p>
		You ordered {scoops} {scoops === 1 ? 'scoop' : 'scoops'}
		of {join(flavours)}
	</p>
{/if}
```