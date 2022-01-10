package github.pancras.service1.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import github.pancras.service1.HttpClient;
import github.pancras.service1.dao.DemoDao;

@Service
public class DemoService {
    @Autowired
    private DemoDao demoDao;

    @Transactional
    public void test() {
        demoDao.insert("server1");
        HttpClient.get("http://localhost:8082/test");
        int i = 100 / 0;
        System.out.println("success insert server1");
    }
}
