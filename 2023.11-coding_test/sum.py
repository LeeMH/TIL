# 첫번째 라인은 문자열의 길이
# 두번째 라인은 숫자로 이뤄진 문자열이 입력된다.
# 각각의 숫자를 더한 값을 출력한다.

def solve(n, str):
    numbers = list(str)

    total = 0
    for ii in numbers:
        total += int(ii)

    return total

n = int(input())
str = input()
print(solve(n, str))