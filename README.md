# Реализация многоэкранного приложения (c Navigation Component)
- для входа, регистрации отображения данных пользователю и настройки интерфейса

## Подключение зависимостей
В build.gradle.kts уровня Модуля требуется добавить зависимость для подключения Navigation Component актуальной версии и плагина для типобезопасных аргументов организуемых маршрутов-переходов:
```
plugins {
    kotlin("plugin.serialization") version "2.0.21" // navigation
}

dependencies {
    implementation("androidx.navigation:navigation-compose:2.10.2")
}
```

## Жизненный цикл и логировании
Для отображения текущего состония жизненного цикла в канал отладки (debug) можно подписаться на реализацию слушателя событий DefaultLifecycleObserver текущего компонента:  
```
val lifecycleOwner = LocalLifecycleOwner.current
val observer = LifecycleEventObserver { source, event ->
        when (event) {
            Lifecycle.Event.ON_PAUSE -> {
                Log.d("TAG", "Screen: ON_PAUSE")
            }
        Lifecycle.Event.ON_ANY -> printCallback("Undefined")
    }
}
// Добавляем наблюдателя к жизненному циклу
lifecycleOwner.lifecycle.addObserver(observer)
```
При этом от данного слушателя необходимо отписаться (удалить).
Или использовать отдельные слушатели событий (LifecycleStartEffect, LifecycleResumeEffect и т.д): 
```
LifecycleEventEffect(Lifecycle.Event.ON_PAUSE) {
    Log.d("TAG", "Screen: ON_PAUSE")
}
```

## Описание маршрутов (переходов)
Для построения навигационного графа требуется описать маршруты переходов между экранами (@Composable-функциями). 

В качестве параметров, описывающих маршрут могут выступать строки, которые можно объединить в перечисление (или объект-компаньон):
```
companion object ActivityRoutes {
    const val A_ROUTE = "screen_a"
    const val B_ROUTE = "screen_b"
}
```
В этом случае аргументы для перехода передаются как часть адреса этого перехода (синтаксис схож с форматом построения URL-адресов):
```
NavHost(rememberNavController(), startDestination = ActivityRoutes.A_ROUTE){
    composable (ActivityRoutes.A_ROUTE) { ScreenA() }
    composable(
        ActivityRoutes.B_ROUTE + "/{id}",
        arguments = listOf(navArgument("id") { type = NavType.IntType })
    ){
        ScreenB ( it.arguments?.getInt("id")
    }
}

ScreenA(){
    var id = 0;
    Text(
        "MOVE",
        Modifier.clickable { navController.navigate("user/${++id}") })
}
ScreenB(id: Int){}
```
Или после подключения плагина сериализации в форме классов с аннотацией kotlinx.serialization.Serializable, описывающих как маршрут перехода, так и требуемые аргументы. Такие классы можно объединять в "запечатанные" классы:
```
sealed class ActivityRoutes {
    @Serializable object ScreenA
    @Serializable data class ScreenB (val param: Any? = null)
}
```
При этом для маршрутов без параметров стоит создавать единственный экземпляр или синглтон (object) в противном случае требуется создание data-класса.
```
NavHost ( rememberNavController(), startDestination = ActivityRoutes.ScreenA() ) {
    composable<ActivityRoutes.ScreenA> { ScreenA() }
    composable<ActivityRoutes.ScreenB> { ScreenB() }
}

ScreenA(){
    var id = 0;
    Text(
        "MOVE",
        Modifier.clickable { navController.navigate(ActivityRoutes.ScreenB(++id)) })
}
ScreenB(id: Int){}
```
При этом параметры передаются непосредственно внутрь функций компонентов.