# Forms

## [The<form> element](https://learn.svelte.dev/tutorial/the-form-element)

* 이전 장에서 서버에서 브라우저로 데이터를 가저오는 방법에 대해 살펴보았습니다.

* 반대로 브라우저에서 서버로 데이터를 보내는 방법을 확인합니다.

* 아래 예제는 todo 항목을 입력하고 엔터를 누르면 서버의 DB로 입력되는 간단한 예제입니다.

src/routes/+page.svelte

```js
<script>
	export let data;
</script>

<h1>todos</h1>

<form method="POST">
	<label>
		add a todo:
		<input name="description" />
	</label>
</form>

<ul>
	{#each data.todos as todo (todo.id)}
		<li class="todo">
			{todo.description}
		</li>
	{/each}
</ul>
```

src/routes/+page.server.js
```js
import * as db from '$lib/server/database.js';

export function load({ cookies }) {
	const id = cookies.get('userid');

	if (!id) {
		cookies.set('userid', crypto.randomUUID(), { path: '/' });
	}

	return {
		todos: db.getTodos(id) ?? []
	};
}

export const actions = {
	default: async({ cookies, request }) => {
		const data = await request.formData()
		db.createTodo(cookies.get('userid'), data.get('description'))
	}
}
```


## [Named form actions](https://learn.svelte.dev/tutorial/named-form-actions)

* 한 페이지에 한개의 작업만 있는 매우 드뭅니다.

* 앞의 예제를 조금 수정하여, `default`를 `create`, `delete` 작업을 추가 합니다.

src/routes/+page.server.js

> actions 하위에 create, delete 메소드 생성

```js
import * as db from '$lib/server/database.js';

export function load({ cookies }) {
	const id = cookies.get('userid');

	if (!id) {
		cookies.set('userid', crypto.randomUUID(), { path: '/' });
	}

	return {
		todos: db.getTodos(id) ?? []
	};
}

export const actions = {
	create: async ({ cookies, request }) => {
		const data = await request.formData();
		db.createTodo(cookies.get('userid'), data.get('description'));
	},

	delete: async ({ cookies, request }) => {
		const data = await request.formData();
		db.deleteTodo(cookies.get('userid'), data.get('id'));
	}
};
```


src/routes/+page.svelte

> form에서 `action` 속성을 통해 명확하게 호출하는 메소드를 선언

> `todo?/create` 이런식으로 입력해야 하지만, 동일한 경로라면 `?/create` 이런식으로 생략할수 있다.


```js
<script>
	export let data;
</script>

<h1>todos</h1>

<form method="POST" action="?/create">
	<label>
		add a todo:
		<input name="description" />
	</label>
</form>

<ul>
	{#each data.todos as todo (todo.id)}
		<li class="todo">
			<form method="POST" action="?/delete">			
			  <input type="hidden" name="id" value={todo.id} />
				<button aria-label="Mark as complete">v</button>
			  {todo.description}
			</form>
		</li>
	{/each}
</ul>
```


## [Validation](https://learn.svelte.dev/tutorial/form-validation)

* 사용자의 실수나 의도적인 공격(?) 방지를 위해 데이터 검증은 매우 중요합니다.

* 첫번째 방어는 브라우저에 내장된 유효성 검사로 가능합니다.

* 예를 들어, input 속성에 required를 쉽게 지정할 수 있습니다.

* 하지만, devtool등으로 쉽게 무력화 할수 있습니다. 따라서 서버사이드 검증이 반드시 필요 합니다.

* 아래는 간단한 검증 샘플 입니다.

* form 제출후 리턴되는 값에 접근하기 위해서는 `form` prop을 통해 접근합니다. (+page.svelte 예제 참고)


src/lib/server/database.js
```js
// In a real app, this data would live in a database,
// rather than in memory. But for now, we cheat.
const db = new Map();

export function getTodos(userid) {
	return db.get(userid);
}

export function createTodo(userid, description) {
    //++++++++++++++++++++++++++++++++++++++
    // 서버검증을 통해 에러를 발생시킨다.
    //++++++++++++++++++++++++++++++++++++++
	if (description === '') {
		throw new Error('todo must have a description');
	}

	if (!db.has(userid)) {
		db.set(userid, []);
	}

	const todos = db.get(userid);

	if (todos.find((todo) => todo.description === description)) {
		throw new Error('todos must be unique');
	}
	
	todos.push({
		id: crypto.randomUUID(),
		description,
		done: false
	});
}

export function deleteTodo(userid, todoid) {
	const todos = db.get(userid);
	const index = todos.findIndex((todo) => todo.id === todoid);

	if (index !== -1) {
		todos.splice(index, 1);
	}
}
```

src/routes/+page.server.js
```js
//++++++++++++++++++++++++++++++++++++++
// fail 을 통해서 실패시 상태코드와 함께 반환가능
//++++++++++++++++++++++++++++++++++++++
import { fail } from '@sveltejs/kit';
import * as db from '$lib/server/database.js';

export function load({ cookies }) {
	const id = cookies.get('userid');

	if (!id) {
		cookies.set('userid', crypto.randomUUID(), { path: '/' });
	}

	return {
		todos: db.getTodos(id) ?? []
	};
}

export const actions = {
	create: async ({ cookies, request }) => {
		const data = await request.formData();
        //++++++++++++++++++++++++++++++++++++++
        // try로 묶은후 에러 발생시 fail을 통해 리턴
        //++++++++++++++++++++++++++++++++++++++
		try {
			db.createTodo(cookies.get('userid'), data.get('description'));
		} catch (error) {
			return fail(422, {
				description: data.get('description'),
				error: error.message
			});
		}
	},

	delete: async ({ cookies, request }) => {
		const data = await request.formData();
		db.deleteTodo(cookies.get('userid'), data.get('id'));
	}
};
```

src/routes/+page.svelte
```js
<script>
	export let data;
    //++++++++++++++++++++++++++++++++++++++
    // form 제출후 응답되는 값은 form 변수를 통해 접근 가능
    //++++++++++++++++++++++++++++++++++++++
	export let form;
</script>

<h1>todos</h1>

{#if form?.error}
	<p class="error">{form.error}</p>
{/if}

<form method="POST" action="?/create">
	<label>
		add a todo:
		<input 
		  name="description" 
		  value={form?.description ?? ''}
		/>
	</label>
</form>

<ul>
	{#each data.todos as todo (todo.id)}
		<li class="todo">
			<form method="POST" action="?/delete">
				<input type="hidden" name="id" value={todo.id} />
				<button aria-label="Mark as complete">✔</button>

				{todo.description}
			</form>
		</li>
	{/each}
</ul>
```

## [Progressive enhancement](https://learn.svelte.dev/tutorial/progressive-enhancement)

* 정확이 이해되지 않는 항목이다. 추후에 업데이트 필요!!

* `enhance` 기능을 활용하기 위해서는 import후 `use:enhance` 지시어를 사용한다.

## [Customizing use:enhance](https://learn.svelte.dev/tutorial/customizing-use-enhance)