# testing
Описание экранов:

1) MainActivity

Splash screen —появляется при старте программы. На экране картинка и надпись
«осталось X сек» - отображает обратный отсчет 5 секунд после чего появляется
главный экран.

2) HomeActivity:

	1. Звезду которая «дышит» (т. е. Раз в 7 секунд размер анимированно увеличивается
	на 25% и затем анимированно уменьшается до исходных размеров). При нажатии
	открывается «google.com» в браузере  Android

	2. Окно с периодический меняющимся названием спорта (список получается вот с
	этого веб сервиса: http://espn.go.com/personalization/feeds/sports.xml).
	Одно название сменяется другим после того как для данного названия спорта были
	показаны все команды в окне «3». При нажатии открывается окно со
	встроенным WebView и отображает страницу «yandex.ru».

	3. Окно с периодический меняющимся названием команды для текущего названия
	спорта из окна «2» (список названий спорта (загруженный в окне «2») содержит
	teamFeed атрибут со ссылкой на список команда для каждого вида спорта). Одно
	название сменяется другим через 4с.
 
	4. Большая картинка со скролингом и увеличением/уменьшение масштаба. На
	которой случайно раскиданы 10 звездочек. При нажатии появляется
	popup диалог с координатами звездочки на которую нажали

	5. Строка в которой выводится выбранный вид спорта и команда. При первом запуске
	ничего не выбрано и строка пустая. Состояние выбранного спорта/команды
	должно запоминаться и сохраняться даже если перезагрузить телефон. При
	нажатии на строку открывается новый экран для выбора команды (смотри экран ActivityTeam )

3) ActivityTeam 
	
	Выбор вида спорта. Список видов спорта получается с веб
сервиса: http://espn.go.com/personalization/feeds/sports.xml. При нажатии на строку
открывается новое окно для выбора команды соответствующей выбранному виду
спорта. (смотри онко «4»). Окно открывается со стандартной анимацией
4) ActivitySport
	 Список команд получается по ссылке из атрибута
teamFeed выбранного вида спорта на предыдущем экране. При нажатии на строку
происходит возврат на главный экран с изменением выбранного спорта и команды. 

Все окна (кроме первого... первое по желанию) делаются отдельными Activity
