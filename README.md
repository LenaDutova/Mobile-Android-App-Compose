# Работа с сетью на базе клиента Retrofit
- для входа, регистрации, отображения данных пользователю и настройки интерфейса

## Подключение зависимостей
В build.gradle.kts уровня Модуля требуется добавить зависимость для подключения сетевого клиента актуальной версии:
```
dependencies {
    implementation("com.squareup.retrofit2:retrofit:3.0.0")
    implementation("com.squareup.retrofit2:converter-gson:3.0.0")
}
```

### Настройка POJO-классов для получения или отправки данных по сети
Retrofit позволяет подключать различные библиотеки для конвератации данных из POJO в формат JSON (или XML). Например, добавим конвертор от Google:
```
dependencies {
    implementation("com.google.code.gson:gson:2.14.0")
}
```

Как и большинство современных библиотек этот конвертер может соотносить название атрибутов в JSON-формате с именами свойств классов (а также конвертировать camelCase в snake_case и обратно). Но в случае значительного расхождения имен, можно воспользоваться аннотацией @SerializedName:
```
import com.google.gson.annotations.SerializedName
data class Joke ( 
    @SerializedName ("identification") val id: Int, 
    val joke: String)
```

### Настройка интерфейса для сетевого клиента
Retrofit требует задать контракт на все методы сетевого обмена, указав в аннотациях направление передачи данных. В аннотациях (как @GET, так и @POST) указывается URL пути обращения (можно полностью, включая HTTP протокол, но обычно имя хоста отделяют). 

Можно добавить заголовок запроса с помощью аннотации @Headers, а такж параметры запроса. Последние встраиваются с помощью @Path и учитывают тип передаваемых данных: 
```
interface JokeRetrofitAPI {
    @GET("joke/Programming")
    fun getJoke(): Call<Joke>

    @GET("joke/Programming?amount={count}}")
    fun getJokes(@Path("count") count: Int): Call<List<Joke>>
}
```
Обертка Call над возвращаемыми типами данных, служит для возможности создания обратных вызовов (Callback).

### Создание и использование сетевого клиента
После настройки инфраструктуры следует создать единственный сетевой клиент в приложении, указав имя хоста, используемый конвертер (и если требуется перечислив имена сериализуемых классов), под описанный ранее контракт (интерфейс):
```
api = Retrofit.Builder()
    .baseUrl("HTTP://BASE_URL/")
    .addConverterFactory(GsonConverterFactory.create())
    .build()
    .create(JokeRetrofitAPI::class.java)    
```

С помощью созданного клиента можно отправлять запросы на сервер, как в синхронном режиме (execute), так и в асинхронном (enqueue). Для использования метода execute() требуется описать два обратных вызова (в случае успешного исполнения и в случае ошибки), которые будут произведены в главном потоке по завершению запроса в сеть:
```
api.getJokes().enqueue(object: Callback<List<Joke>> {
    override fun onFailure(call: Call<List<Joke>>, throwable: Throwable) {
       Log.d("TAG", throwable.message)
    }

    override fun onResponse(call: Call<List<Joke>>, response: Response<List<Joke>>) {
        if (response.isSuccessful){
            val list = response.body() as MutableList<Joke>
            list.forEach { Log.d("TAG", it.toString()) }
        } else {
            Log.d("TAG", response.code())
        }
    }
}
```

При использовании метода execute(), его потребуется самостоятельно вызвать в фоновом потоке, например, с помощью корутины, также в этом случае в интерфейса должно присутствовать ключевое слово suspend:
```
// suspend fun getJokes(@Path("count") count: Int): Response<List<Joke>>
lifecycleScope.launch {
    runCatching {
        withContext(Dispatchers.IO){
            api.getJokes().body()
        }
    }.onSuccess { jokes ->
        jokes.forEach { Log.d("TAG", it.toString()) }
    }.onFailure { throwable ->
        Log.d("TAG", throwable.message)
    }
}
```

Получаемые по сети данные можно как передать в @Composable-функцию как аргумент, но гораздо лучше связать такие параметры с состоянием компонента, для непосредственного обновления данных:
```
val jokes = remember { mutableStateListOf<Joke>() }
val acoount = remember { mutableStateOf("Anonymous") }
```

## Navigation Component, version 3

С данной ветки проекта используется библиотека Navigation 3. В build.gradle.kts уровня Проекта требуется добавить зависимость для подключения плагина

```
plugins {
    alias("org.jetbrains.kotlin.plugin.serialization:2.4.20") apply false
}
```

В build.gradle.kts уровня Модуля требуется добавить зависимость для подключения Navigation Component актуальной версии и плагина для типобезопасных аргументов организуемых маршрутов-переходов:
``` 
plugins {
//    alias(libs.plugins.kotlin.android)  // navigation
    alias("org.jetbrains.kotlin.plugin.serialization:2.4.20")
}
dependencies {
//    implementation("androidx.navigation:navigation-compose:2.10.2")
    implementation("androidx.navigation3:navigation3-runtime:1.2.0")
    implementation("androidx.navigation3:navigation3-ui:1.2.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:1.11.0")
}
```

### Описание маршрутов (переходов)
Для построения навигационного графа требуется описать маршруты переходов между экранами (@Composable-функциями).

В качестве параметров, описывающих маршрут выступают объекты, наследуемые от класса androidx.navigation3.runtime.NavKey. Как и в предыдущей версии это могут быть object или data class (если переход предполагает передачу параметров):
```
import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

sealed interface SingleActivityRoutes : NavKey {

    @Serializable data object ARoute : SingleActivityRoutes
    @Serializable data class BRoute (val id: Int) : SingleActivityRoutes
} 
```
В этом случае граф навигации строится не на основании функций NavHost и composable(), а используя NavDisplay, позволяющего более детальную настройку маршрутов и харатера переходов между ними:
```
val backStack = rememberNavBackStack(SingleActivityRoutes.ARoute())
NavDisplay( 
    backStack = backStack,
    onBack = {
        if (backStack.size > 1) backStack.removeAt(backStack.lastIndex)
        else finish()
    },
    entryProvider = entryProvider {
        entry<SingleActivityRoutes.ScreenA> { route -> ScreenA() }
        entry<SingleActivityRoutes.ScreenB> { route -> ScreenB ( id = route.id) }
    }
)

ScreenA(){
    var id = 0;
    Text(
        "MOVE",
        Modifier.clickable { backStack.add(SingleActivityRoutes.ScreenB(id)) })
}
ScreenB(id: Int){}
```