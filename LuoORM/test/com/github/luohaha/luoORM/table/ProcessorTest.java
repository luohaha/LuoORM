package com.github.luohaha.luoORM.table;

import com.github.luohaha.luoORM.define.RowValue;
import com.github.luohaha.luoORM.define.RowValueAndTable;
import com.github.luohaha.luoORM.exception.ClassNotExistAnnotation;

public class ProcessorTest {
    @Table(TableName="Book")
    static class Book {
        private String name;
        private int price;

        public Book(String name, int price) {
            this.name = name;
            this.price = price;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Book book = (Book) o;

            if (price != book.price) return false;
            return name != null ? name.equals(book.name) : book.name == null;
        }

        @Override
        public int hashCode() {
            int result = name != null ? name.hashCode() : 0;
            result = 31 * result + price;
            return result;
        }
    }

    public static void main(String[] args) throws ClassNotExistAnnotation {
        Book book = new Book("one world", 120);
        RowValueAndTable rowValueAndTable1 = Processor.TableToRV(book);

        RowValue rowValue = RowValue.create();
        rowValue.set("name", "one world");
        rowValue.set("price", 120);
        RowValueAndTable rowValueAndTable2 = new RowValueAndTable(rowValue, "Book");

        if (rowValueAndTable1.equals(rowValueAndTable2)) {
            System.out.println("success");
        } else {
            System.out.println("fail");
        }
    }
}
