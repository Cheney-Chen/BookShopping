/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.imp;

import dao.IBookDAO;
import domain.Book;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.DataSource;

/**
 *
 * @author Amy
 */
public class BookDAO implements IBookDAO {

    final private DataSource dataSource;

    public BookDAO(final DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    synchronized public void addBook(final Book book) {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = dataSource.getConnection();
            stmt = conn.prepareStatement("INSERT INTO book(BOOK_NAME,BOOK_AUTHOR,BOOK_PUBLISHER,BOOK_PRICE,BOOK_DATE) VALUES(?,?,?,?,?)");//插入資料
            stmt.setString(1, book.getBook_Name());
            stmt.setString(2, book.getBook_Author());
            stmt.setString(3, book.getBook_Publisher());
            stmt.setInt(4, book.getBook_Price());
             stmt.setTimestamp(5, new Timestamp(new Date().getTime()));
            stmt.executeUpdate();
        } catch (SQLException ex1) {
            throw new RuntimeException(ex1);
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException ex) {
                    Logger.getLogger(BookDAO.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException ex1) {
                    Logger.getLogger(BookDAO.class.getName()).log(Level.SEVERE, null, ex1);
                }
            }


        }

//        File bookFile=new File("/home/base/BookStore");
//        bookFile.mkdir();
//        BufferedWriter writer = null;
//        try {
//            writer = new BufferedWriter(new FileWriter(bookFile+"/"+book.getID()));
//            writer.write(book.getID()+"\t"+book.getName()+"\t"+book.getAuthor()+"\t"+book.getPublisher()+"\t"+book.getPrice());
//            writer.close();
//        } catch (IOException ex) {
//            Logger.getLogger(BookDAO.class.getName()).log(Level.SEVERE, null, ex);
//        }  

    }

    @Override
    public String runBookID() throws IOException {

        File folder = new File("/home/base/BookStore");//定義資料型態
        folder.mkdir();
        File[] listOfFiles = folder.listFiles();//產生新的陣列     

        return Integer.toString(listOfFiles.length + 1);
    }

    @Override
    public Set<Book> getAllBooks() {
        Set<Book> bookSet = new TreeSet<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = dataSource.getConnection();
            stmt = conn.prepareStatement("SELECT BOOK_ID,BOOK_NAME,BOOK_AUTHOR,BOOK_PUBLISHER,BOOK_PRICE FROM BOOK");//select all
            rs = stmt.executeQuery();

            while (rs.next()) {
                bookSet.add(new Book(rs.getInt("BOOK_ID"), rs.getString("BOOK_NAME"), rs.getString("BOOK_AUTHOR"), rs.getString("BOOK_PUBLISHER"), rs.getInt("BOOK_PRICE")));
            }
        } catch (SQLException ex1) {
            throw new RuntimeException(ex1);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(BookDAO.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException ex) {
                    Logger.getLogger(BookDAO.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException ex1) {
                    Logger.getLogger(BookDAO.class.getName()).log(Level.SEVERE, null, ex1);
                }
            }

        }
        return bookSet;
    }

    @Override
    public Book getBookByID(final Integer id) {
        Connection conn = null;
        PreparedStatement stmt = null;
        Book book = null;
        ResultSet rs = null;
        try {
            conn = dataSource.getConnection();
            stmt = conn.prepareStatement("SELECT BOOK_ID,BOOK_NAME,BOOK_AUTHOR,BOOK_PUBLISHER,BOOK_PRICE FROM BOOK WHERE BOOK_ID=?");
            stmt.setInt(1, id);
            rs = stmt.executeQuery();
            while (rs.next()) {//必須要有游標next才取的到值
                book = new Book(rs.getInt("BOOK_ID"), rs.getString("BOOK_NAME"), rs.getString("BOOK_AUTHOR"), rs.getString("BOOK_PUBLISHER"), rs.getInt("BOOK_PRICE"));
            }
        } catch (SQLException ex1) {
            throw new RuntimeException(ex1);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(BookDAO.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException ex) {
                    Logger.getLogger(BookDAO.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException ex1) {
                    Logger.getLogger(BookDAO.class.getName()).log(Level.SEVERE, null, ex1);
                }
            }

        }
        return book;
//        File folder = new File("/home/base/BookStore");//定義資料型態
//         folder.mkdir();
//         Book book =new Book();
//         BufferedReader reader=null;
//
//            reader=new BufferedReader(new FileReader(folder+"/"+id));
//            String[] line=null;
//            line=reader.readLine().split("\t");
//            book.setID(line[0]);
//            book.setName(line[1]);
//            book.setAuthor(line[2]);
//            book.setPublisher(line[3]);
//            book.setPrice(Integer.parseInt(line[4]));
//   

    }

    @Override
    public boolean isBookExisted(final Book book) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        boolean existed = false;

        try {
            conn = dataSource.getConnection();
            stmt = conn.prepareStatement("SELECT COUNT(1) FROM BOOK WHERE BOOK_NAME=?");
            stmt.setString(1, book.getBook_Name());
            rs = stmt.executeQuery();
            while (rs.next()) {//必須要有游標next才取的到值
                existed = (rs.getInt(1) >= 1);
            }
        } catch (SQLException ex1) {
            Logger.getLogger(BookDAO.class.getName()).log(Level.SEVERE, null, ex1);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex1) {
                    Logger.getLogger(BookDAO.class.getName()).log(Level.SEVERE, null, ex1);
                }
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException ex1) {
                    Logger.getLogger(BookDAO.class.getName()).log(Level.SEVERE, null, ex1);
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException ex1) {
                    Logger.getLogger(BookDAO.class.getName()).log(Level.SEVERE, null, ex1);
                }
            }
        }
        return existed;
    }

    @Override
    public Set<Book> getAllBooks(final Integer page, final Integer size) {
        Set<Book> bookSet = new TreeSet<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = dataSource.getConnection();
            stmt = conn.prepareStatement("SELECT BOOK_ID,BOOK_NAME,BOOK_AUTHOR,BOOK_PUBLISHER,BOOK_PRICE FROM  (select row_number() over() as rownum, BOOK_ID,BOOK_NAME,BOOK_AUTHOR,BOOK_PUBLISHER,BOOK_PRICE from BOOK) as tmp where rownum>=? and rownum<=?");//以每頁size得到第page頁的內容
            stmt.setInt(1, (page - 1) * size + 1);
            stmt.setInt(2, page * size);
            rs = stmt.executeQuery();
            int rowForPagingCount = 0;
            while (rs.next() && rowForPagingCount < size) {
                bookSet.add(new Book(rs.getInt("BOOK_ID"), rs.getString("BOOK_NAME"), rs.getString("BOOK_AUTHOR"), rs.getString("BOOK_PUBLISHER"), rs.getInt("BOOK_PRICE")));
                rowForPagingCount ++;
            }
        } catch (SQLException ex1) {
            throw new RuntimeException(ex1);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex1) {
                    Logger.getLogger(BookDAO.class.getName()).log(Level.SEVERE, null, ex1);
                }
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException ex1) {
                    Logger.getLogger(BookDAO.class.getName()).log(Level.SEVERE, null, ex1);
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException ex1) {
                    Logger.getLogger(BookDAO.class.getName()).log(Level.SEVERE, null, ex1);
                }
            }
        }
        return bookSet;
    }

    @Override
    public Integer getBooksCount() {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        int booksCount = 0;
        try {
            conn = dataSource.getConnection();
            stmt = conn.prepareStatement("SELECT COUNT(*) FROM BOOK");//查詢筆數
            rs = stmt.executeQuery();

            if (rs.next()) {
                booksCount = rs.getInt(1);
            }
        } catch (SQLException ex1) {
            throw new RuntimeException(ex1);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex1) {
                    Logger.getLogger(BookDAO.class.getName()).log(Level.SEVERE, null, ex1);
                }
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException ex1) {
                    Logger.getLogger(BookDAO.class.getName()).log(Level.SEVERE, null, ex1);
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException ex1) {
                    Logger.getLogger(BookDAO.class.getName()).log(Level.SEVERE, null, ex1);
                }
            }
        }
        return booksCount;
    }
}
