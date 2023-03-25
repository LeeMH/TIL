# Reactivity

## [Assignments](https://learn.svelte.dev/tutorial/reactive-assignments)

* DOM 이벤트에 핸들러를 적용하고, 업데이트 되는 간단한 예제를 보여준다.

```js
<script>
	let count = 0;

	function increment() {
		count += 1
	}
</script>

<button on:click={increment}>
	Clicked {count}
	{count === 1 ? 'time' : 'times'}
</button>
```

## [Declarations](https://learn.svelte.dev/tutorial/reactive-declarations)

* 직접이 아니더라도, 간접적으로 변경이 되었다면 연쇄적으로 값이 변경되어야 한다.

* 이럴때, `$:` 키워드를 이용할수 있다.

```js
<script>
	let count = 0
  $: doubled = count * 2

	function increment() {
		count += 1;
	}
</script>

<button on:click={increment}>
	Clicked {count}
	{count === 1 ? 'time' : 'times'}
</button>

<p>{count} doubled is {doubled}</p>
```

## [Statements](https://learn.svelte.dev/tutorial/reactive-statements)

* 반응형 구문은 꼭 변수의 값 변경뿐만 아니라, 콘솔아웃, 블럭(마치 함수), if구문등에도 사용될수 있음을 설명한다.

```js
<script>
	let count = 0;

  $: {
    console.log(`the count is ${count}`)
    alert(`I SAID THE COUNT is ${count}`)
  }

	function handleClick() {
		count += 1;
	}

  
</script>

<button on:click={handleClick}>
	Clicked {count}
	{count === 1 ? 'time' : 'times'}
</button>
```

## [Updating arrays and objects](https://learn.svelte.dev/tutorial/updating-arrays-and-objects)

* Svelte의 반응성은 할당에 의해 트리거 됨을 설명

* 재할당을 통해 반응성을 얻을수 있음.

```js
<script>
	let numbers = [1, 2, 3, 4]

	function addNumber() {
		numbers.push(numbers.length + 1)
    numbers = numbers

    // numbers = [...numbers, numbers.length + 1]
	}

	$: sum = numbers.reduce((t, n) => t + n, 0);
</script>

<p>{numbers.join(' + ')} = {sum}</p>

<button on:click={addNumber}>
	Add a number
</button>
```

* 간단하게 설명하면, 좌측에 업데이트 되는 변수가 나와야 합니다.

* 아래의 예는 반응성이 나타나지 않습니다.

```js
const foo = obj.foo
foo.bar = 'baz'
```

* 반응성이 나타나려면 아래와 같아야 합니다.

```js
obj = obj
```