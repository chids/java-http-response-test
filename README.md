
= Example

To play around with nginx's upstream module

1. launch a couple of jetty's in the root of this project:

    mvn -Djetty.port=9997 jetty:run
    mvn -Djetty.port=9998 jetty:run
    mvn -Djetty.port=9999 jetty:run

2. configure nginx:

    upstream jetty {
        server 127.0.0.1:9997;
        server 127.0.0.1:9998;
        server 127.0.0.1:9999;
    }

    server {
        listen 8080;
        server_name 127.0.0.1;
        proxy_next_upstream error timeout invalid_header http_500 http_502 http_503 http_504;
        location / {
            proxy_pass                  http://jetty;
            proxy_redirect              off;
            proxy_buffering             off;
            proxy_set_header            host            $http_host;
            proxy_set_header            x-real-ip       $remote_addr;
        }
    }

3. do some requests:

    while [ 1 ]; do curl http://127.0.0.1:8080/; done


4. test stuff such as
- bring up a JMX console against the jetty instances and play around with various combinations of response codes until you find something that works in this dust
- mark one or more of the upstream servers as "backup" in nginx.conf, reload the configuration and play with the response codes