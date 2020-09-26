package china.se.integrate.integration.controller;

import china.se.integrate.integration.data.MysqlJdbc;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author hmb
 */

@RestController()
@RequestMapping("/controller")
public class MysqlController {


    MysqlJdbc dao;


    @GetMapping("/getTableColumn")
    public void listTableName(){
        dao.getAllTables("");
    }
}
