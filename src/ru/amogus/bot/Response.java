package ru.amogus.bot;

public enum Response {
    HELLO("Привет!\n" +
            "Чтобы ознакомиться с функционалом бота, отправь /help!"),
    HELP("Список доступных команд:\n" + "/randompoem - случайный экземпляр из коллекции русской поэзии!\n"),
    UNREADABLE_BARCODE("К сожалению, не удалось распознать штрихкод\uD83D\uDE14\n" +
            "Попробуй отправить ещё одно фото"),
    INVALID_ISBN("Данный ISBN является инвалидным!"),
    WRONG_PHOTO_FORMAT("К сожалению, я не могу обработать файл такого расширения." +
            "\nОтправь мне компрессированное (сжатое) фото или файл с расширением .bmp, .gif, .jpg или .png!"),
    UNKNOWN_ISBN("К сожалению, по присланному ISBN не удалось найти никакой книги.\nЕсли на изображении со штрихкодом код ISBN отличен от самого штрихкода, то " +
            "попробуй отправить мне именно этот код!\nЕсли по обоим кодам не удаётся найти книгу, то скорее всего этого издания нет на KnigaBook.com");

    private final String content;
    Response(String content){
        this.content = content;
    }
    public String getContent(){ return content; }
}
