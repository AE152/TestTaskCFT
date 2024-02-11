import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Comparator;

public class MyClass {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Usage: java MyClass [-p <prefix>] <file1> <file2> ...");
            return;
        }
        
        ArrayList<String> argList = new ArrayList<>(List.of(args)); // Преобразуем массив args в ArrayList
        
        boolean shortStatisticsFlag = false;
        boolean fullStatisticsFlag = false;
        boolean appendFlag = false; // Режим дополнения
        String prefix = ""; // Инициализируем переменную префикса значением по умолчанию

        // Поиск флага -s в аргументах командной строки
        if (argList.contains("-s")) {
            shortStatisticsFlag = true;
            argList.remove("-s"); // Удаляем флаг -s из списка аргументов
        }

        // Поиск флага -f в аргументах командной строки
        if (argList.contains("-f")) {
            shortStatisticsFlag = false;
            fullStatisticsFlag = true;
            argList.remove("-f"); // Удаляем флаг -f из списка аргументов
        }

        // Поиск флага -a в аргументах командной строки
        if (argList.contains("-a")) {
            appendFlag = true;
            argList.remove("-a"); // Удаляем флаг -a из списка аргументов
        }

        // Поиск флага -o и его значения в аргументах командной строки
        for (int i = 0; i < argList.size(); i++) {
            if (argList.get(i).equals("-o")) {
                if (i + 1 < argList.size()) {
                    prefix += argList.get(i + 1);
                    prefix += "/";
                    argList.remove(i); // Удаляем флаг -o из списка аргументов
                    argList.remove(i); // Удаляем его значение из списка аргументов
                    break;
                } else {
                    System.out.println("Invalid usage of -o flag.");
                    return;
                }
            }
        }
        
        // Поиск флага -p и его значения в аргументах командной строки
        for (int i = 0; i < argList.size(); i++) {
            if (argList.get(i).equals("-p")) {
                if (i + 1 < argList.size()) {
                    prefix += argList.get(i + 1);
                    argList.remove(i); // Удаляем флаг -p из списка аргументов
                    argList.remove(i); // Удаляем его значение из списка аргументов
                    break;
                } else {
                    System.out.println("Invalid usage of -p flag.");
                    return;
                }
            }
        }

        // Определение имён файлов с учётом префикса
        ArrayList<String> names_files = new ArrayList<>();
        names_files.add(prefix + "integers.txt");
        names_files.add(prefix + "doubles.txt");
        names_files.add(prefix + "strings.txt");

        ArrayList<Long> integers = new ArrayList<>();
        ArrayList<Double> doubles = new ArrayList<>();
        ArrayList<String> strings = new ArrayList<>();

        // Обработка файлов
        for (String fileName : argList) {
            readFromFile(fileName, integers, doubles, strings);
        }

        writeListToFile(integers, names_files.get(0), appendFlag);
        writeListToFile(doubles, names_files.get(1), appendFlag);
        writeListToFile(strings, names_files.get(2), appendFlag);

        printStatistics(fullStatisticsFlag, shortStatisticsFlag, integers, doubles, strings);
    }

    public static void writeListToFile(List<?> list, String filePath, boolean appendMode) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, appendMode))) {
            for (Object item : list) {
                writer.write(item.toString());
                writer.newLine(); // Добавляем новую строку после каждого элемента
            }
        } catch (IOException ex) {
            System.out.println("Error writing to file: " + ex.getMessage());
        }
    }

    public static void readFromFile(String fileName, ArrayList<Long> integers, ArrayList<Double> doubles, ArrayList<String> strings){
        Path src = Path.of(fileName);
        List<String> str = new ArrayList<>();
        try{
            str = Files.readAllLines(src);
        }catch(IOException ex){
            System.out.println("File not found! " + ex.getMessage());
        }
        
        for (String s : str){
            try{
                Long tmp_int = Long.parseLong(s);
                integers.add(tmp_int);
            } catch(NumberFormatException e1){
                try {
                    Double tmp_doub = Double.parseDouble(s);
                    doubles.add(tmp_doub);
                } catch(NumberFormatException e2){
                    strings.add(s);
                }
            }
        }
    }

    public static void printStatistics(boolean fullStatisticsFlag, boolean shortStatisticsFlag, ArrayList<Long> integers,
                                         ArrayList<Double> doubles, ArrayList<String> strings)
    {
        String res_str = "";
        if(fullStatisticsFlag){
            res_str += "Full statistics:\n";
            if(!integers.isEmpty()){
                Long sum_int = 0L;
                for(Long i : integers){
                    sum_int += i;
                }
                res_str += "\nintegers:\ncount: " + Long.toString(integers.size()) +
                "\nmax: " + Collections.max(integers).toString() + "\nmin: " + Collections.min(integers).toString() + 
                "\nsum: " + sum_int.toString() + "\naverage: " + Long.toString(sum_int/integers.size()) + "\n";
            }
            if(!doubles.isEmpty()){
                Double sum_doub = 0.0;
                for(Double i : doubles){
                    sum_doub += i;
                }
                res_str += "\ndoubles:\ncount: " + Long.toString(doubles.size()) +
                "\nmax: " + Collections.max(doubles).toString() + "\nmin: " + Collections.min(doubles).toString() + 
                "\nsum: " + sum_doub.toString() + "\naverage: " + Double.toString(sum_doub/doubles.size()) + "\n";
            }
            if(!strings.isEmpty()){
                res_str += "\nstrings:\ncount: " + Long.toString(strings.size()) +
                "\nmax_length: " + Collections.max(strings, Comparator.comparing(s -> s.length())).toString() + 
                "\nmin_length: " + Collections.min(strings, Comparator.comparing(s -> s.length())).toString() + "\n";
            }
            System.out.println(res_str);
        }else{
            if(shortStatisticsFlag){
                res_str += "Short statistics:\n";
                if(!integers.isEmpty()){
                    res_str += "\nintegers:\ncount: " + Long.toString(integers.size()) + "\n";
                }
                if(!doubles.isEmpty()){
                    res_str += "\ndoubles:\ncount: " + Long.toString(doubles.size()) + "\n";
                }
                if(!strings.isEmpty()){
                    res_str += "\nstrings:\ncount: " + Long.toString(strings.size()) + "\n";
                }
                System.out.println(res_str);
            }
        }
    }
}
