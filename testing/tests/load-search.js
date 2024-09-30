import http from 'k6/http'
import { check } from 'k6'


export const options = {
    insecureSkipTLSVerify: true,
    discardResponseBodies: false,
    scenarios: {
        warmingUp : {
            executor: 'per-vu-iterations',
            vus: 1,
            iterations: 50,
            maxDuration: '60m',
            startTime: '5s',
        },
        rampUp : {
            executor: 'per-vu-iterations',
            vus: 5,
            iterations: 350,
            maxDuration: '60m',
            startTime: '20s',
        },
    },
};

const searchTerms = [
    '?brandId=1&productId=35455&applicationDate=2020-06-14T10:00:00',
    '?brandId=1&productId=35455&applicationDate=2020-06-14T16:00:00',
    '?brandId=1&productId=35455&applicationDate=2020-06-14T21:00:00',
    '?brandId=1&productId=35455&applicationDate=2020-06-15T10:00:00',
    '?brandId=1&productId=35455&applicationDate=2020-06-16T21:00:00',
]

export default function() {

    const payload = searchTerms[Math.floor(Math.random()*searchTerms.length)];

    const response = http.get(`http://price-api/api/prices${payload}`,  {
        headers: {
            'accept': 'application/json',
            'content-type': 'application/json',
        }
    });

    check(response, {
        'status is OK': (r) => {
            r.status === 200
        }
    })
}
