import axios from '../../plugins/axios'

let apis = {
  getDbList: (alias) => {
    return axios.get('/api/' + alias + '/db_list')
  },
  getDbKeyList: (alias, db) => {
    return axios.get('/api/' + alias + '/' + db + '/key_list')
  }
}

export default apis
