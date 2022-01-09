package github.pancras.service2.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import github.pancras.service2.dao.DemoDao;

@Service
public class DemoService {
    @Autowired
    private DemoDao demoDao;

    @Transactional
    public void test() {
        demoDao.insert("server2");
        System.out.println("success insert server2");
    }
}
