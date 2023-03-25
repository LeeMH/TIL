# Concepts

## [What is SvelteKit?](https://learn.svelte.dev/tutorial/introducing-sveltekit)

* SvelteKit은 고성능 웹 프레임워크 입니다.

* Svelte가 컴포넌트 프레임워크인 반면, SvelteKit은 까다로운 문제를 해결할수 있는 완성된 app 프레임워크 입니다.

1) Routing

2) Server-side rendering

3) Data fetching

4) Server workers

5) TypeScript integration

6) Prefrendering

7) Single-page apps

8) Library packaging

9) Optimised productions builds

10) Deploying to different hosting providers

11) ...and so on

* SvelteKit은 최적화된 첫페이지 로딩과 SEO를 위해서 디폴트로 SSR방식으로 동작합니다.


## [Project structure](https://learn.svelte.dev/tutorial/project-structure)

* svelte.config.js에는 프로젝트 설정이 포함되어 있습니다. 자세한 사항은 [문서](https://kit.svelte.dev/docs/configuration)를 참고 하십시요.

* src 폴더는 app 폴더 입니다. src/app.html은 페이지 템플릿이고, %sveltekit.head%와 %sveltekit.body% 대체됩니다.

* src/routes에는 routes 정보가 있습니다.

* 마지막으로 static 폴더는 정적 리소스가 탑재되며 app과 함께 배포됩니다.


## [Server and client](https://learn.svelte.dev/tutorial/server-and-client)

* SvelteKit은 서버, 클라이언트로 구분되는 2개의 엔티티가 작동하는 시스템 입니다.

* 클라이언트는 브라우저에 로드되는 javascript 입니다.

* 서버는 정적인 파일로 배포되지만, 클라이언트의 요청을 응답으로 변환해서 응답하는 역할을 합니다.


