# Errors and redirects

## [GET handler](https://learn.svelte.dev/tutorial/get-handlers)

* `+server.js`에 HTTP method GET, POST, PUT, DELETE, PATCH를 추가하여 api route를 할수 있습니다.

```js
import { json } from '@sveltejs/kit';

export function GET() {
	const number = Math.floor(Math.random() * 6) + 1;

	return json(number);
}
```

```js
<script>
	/** @type {number} */
	let number;

	async function roll() {
		const response = await fetch('/roll');
		number = await response.json();
	}
</script>

<button on:click={roll}>Roll the dice</button>

{#if number !== undefined}
	<p>You rolled a {number}</p>
{/if}
```