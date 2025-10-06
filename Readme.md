ТЗ: 
![IMAGE 2025-10-02 10:23:53.jpg](screenshots/IMAGE%202025-10-02%2010%3A23%3A53.jpg)

запуск кластера: docker compose up -d

Порядок выполнения тз:

1) ShopAPI:

а) Создание сущностей для парсинга json-объекта:
`@Data
@NoArgsConstructor
@AllArgsConstructor
public class Item {
    @JsonProperty("product_id")
    private String productId;
    private String name;
    private String description;
    private Price price;
    private String category;
    private String brand;
    private Stock stock;
    private String sku;
    private List<String> tags;
    private Map<String, String> specifications;
    @JsonProperty("created_at")
    private LocalDateTime createdAt;
    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;
    private String index;
    @JsonProperty("store_id")
    private String storeId;
}`

`@Data
@NoArgsConstructor
@AllArgsConstructor
public class Price {
    private String currency;
    private Long amount;
}`

`@Data
@NoArgsConstructor
@AllArgsConstructor
public class Stock {
    private Long available;
    private Long reserved;
}`

б) ShopController - рест-контроллер для получения новых товаров.
в) ProductValidationService - сервис валидации цензуры новых товаров.
г) KafkaStreamsConfiguration - конфиг кафка - стримов, отвечающих за потоковую обработку сообщений. В нашем случае валидация товаров
и дальнейшее перенаправление в другие топики:
- products-input - начальный топик, туда попадают абсолютно все товары;
- new_items - туда попадают успешно прошедшие валидацию товары;
- rejected-items - топик для нецензурных товаров.

Протестим работу сервиса:
а) Отправим леквидный json через пост-запрос на эндпоинт http://localhost:8081/products/send_new_item:

`{
"product_id": "test-003",
"name": "товар",
"description": "Обычное описание",
"price": {"amount": 1000, "currency": "RUB"},
"category": "Тест",
"brand": "TestBrand",
"stock": {"available": 10, "reserved": 0},
"sku": "TEST-001",
"index": "products",
"store_id": "store_test"
}`

Зайдем в PostMan и отправим post-запрос с соответствующим телом по указанномму юрлу:
![IMAGE 2025-09-30 12:50:18.jpg](screenshots/IMAGE%202025-09-30%2012%3A50%3A18.jpg)

Перейдем на юрл http://localhost:8080, посмотрим топики и проверим каждый из них:
![IMAGE 2025-09-30 12:58:03.jpg](screenshots/IMAGE%202025-09-30%2012%3A58%3A03.jpg)

Заглянем в топики products-input и new_items по очереди:
![IMAGE 2025-09-30 13:01:47.jpg](screenshots/IMAGE%202025-09-30%2013%3A01%3A47.jpg)
-------------------------------------------------------------------------------------------
![IMAGE 2025-09-30 13:03:31.jpg](screenshots/IMAGE%202025-09-30%2013%3A03%3A31.jpg)

Видим, что новый айтем попал в оба топика и, самое главное для проверки работы функционала, он попал в топик
new_items, что значит для нас, что кафка-стримы успешно провалидировали его и отправили для дальнейшей обработки.

Теперь отправим невалидный айтем:
в теле пост-запроса передадим json с одним из запрещенных слов в названии самого айтема, описании, категории и названии бренда.
**forbidden-words: "запрещенный,паленый,контрбандный,неликвидный,нелегальный,бракованный"**
![IMAGE 2025-09-30 13:53:03.jpg](screenshots/IMAGE%202025-09-30%2013%3A53%3A03.jpg)

Посмотрим топики products-input и rejected-items соответственно:
![IMAGE 2025-09-30 13:55:59.jpg](screenshots/IMAGE%202025-09-30%2013%3A55%3A59.jpg)
-------------------------------------------------------------------------------------------
![IMAGE 2025-09-30 13:56:57.jpg](screenshots/IMAGE%202025-09-30%2013%3A56%3A57.jpg)

Убедились, что все ок.

2) ClientApi:
a) Модели для сохранения информации по товарам и заказам:
![IMAGE 2025-10-02 09:34:07.jpg](screenshots/IMAGE%202025-10-02%2009%3A34%3A07.jpg)

б) NewItemsConsumer - консюмер, читающий из топика new_items новые айтемы и сохраняющий их в БД.
в) OrderService - сервис, принимающий заказы клиентов и передающий их дальше в топик ordered_items

Итак, протестим работу!
Мы уже отправили через ShopAPI json валидного товара -> ?что же делает ClientAPI?
Вот логи в микросервисе ClientAPI:

`2025-10-02T09:44:56.639+03:00  INFO 23224 --- [Client] [ntainer#0-0-C-1] c.e.Client.consumer.NewItemsConsumer     : Received: item Item(.....)`

`Hibernate:
insert
into
items
(amount, brand, category, created_at, currency, description, name, product_id, specifications, tags, updated_at)
values
(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)`

`2025-10-02T09:44:56.805+03:00  INFO 23224 --- [Client] [ntainer#0-0-C-1] c.e.Client.services.NewItemsService      : Новый товар успешно сохранен в БД: ItemEntity(.....)`

В то же время таблица Items:
![IMAGE 2025-10-02 09:48:47.jpg](screenshots/IMAGE%202025-10-02%2009%3A48%3A47.jpg)
Инфа успешно сохранилась!

Теперь сделаем заказ от имени пользователя с id 3, отправив на юрл http://localhost:8082/order/make-an-order/{userId}/{itemId} 
числовые значения айдишников в фигурных скобках:
![IMAGE 2025-10-02 09:56:54.jpg](screenshots/IMAGE%202025-10-02%2009%3A56%3A54.jpg)

Логи сервиса ClientAPI:
`025-10-02T09:55:11.516+03:00  INFO 23224 --- [Client] [nio-8082-exec-2] c.example.Client.services.OrderService   : Информация по заказу common.vars.dto.OrderEvent@2421ebd1 отправлена в топик.`

Данные о новом заказе были отправлены в топик ordered_items и сохранены в БД, вот 2 скриншота по порядку:
![IMAGE 2025-10-02 10:00:21.jpg](screenshots/IMAGE%202025-10-02%2010%3A00%3A21.jpg)
_____________________________________________________________________________________________________________________________________________
![IMAGE 2025-10-02 10:01:14.jpg](screenshots/IMAGE%202025-10-02%2010%3A01%3A14.jpg)



!!!!!Правки 03.10 - 04.10!!!!!!
1) Теперь в микросервисе Shop в классе ShopService.java асинхронная отправка сообщения в топик, убрал блокирующий метод .get()
2) В микросервисе Client в классе NewItemsConsumer.java добавил обработку ошибок чтения из топика, информация о проблемных сообщениях теперь 
отправляется в DLQ.
3) В микросервисе Client в классе OrderService.java отправка в кафка-топик ordered_items и сохранение нового заказа в БД происходят
консистентно, в случае сбоя на одной из сторон, сообщение не будет ни сохранено в БД, ни отправлено в топик.
4) Настроил безопасную передачу сообщений по кафке. Теперь это происходит с использованием TLS-сертификации.
5) Настроил метрики. Прописал правильный конфиг для Prometheus, создал дашборды для Grafana.


