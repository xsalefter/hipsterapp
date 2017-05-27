import { Injectable } from '@angular/core';
import { Http, Response} from '@angular/http';
import { Observable } from 'rxjs/Rx';

@Injectable()
export class ElasticSearchService {

    constructor(private http: Http) {
    }

    reIndex() {
        console.log('elastic search data will re-indexing.');
        this.http
            .post('api/elasticsearch/reindex', {})
            .map((res: Response) => res) // no need .json because response is plain text
            .subscribe();
    }
}
