/**
 * Created by cl on 2017/10/11.
 */
class TempScript {

    public static void main(String[] args) {
        // win10聚焦壁纸临时文件重命名
        def file = new File("D:\\wallpaper");
        file.eachFile({ it ->
//            println "文件名：${it.getAbsolutePath()}.jpg，大小：${it.length() / 1024} KB";
            if (it.length() / 1024 < 150) {
                it.delete();
            }
            it.renameTo(new File(it.getAbsolutePath() + ".jpg"));
        });
    }

}
