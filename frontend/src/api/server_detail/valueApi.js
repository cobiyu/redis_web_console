import axios from '../../plugins/axios'

let apis = {
  getValue: (alias, db, key) => {
    return axios.get('/api/' + alias + '/' + db + '/data/' + key)
  },
  addKey: (alias, db, key, value, ttl) => {
    return axios.post('/api/' + alias + '/' + db + '/data', {
      key: key,
      value: value,
      ttl: ttl
    })
  },
  modifyTtl: (alias, db, key, ttl) => {
    return axios.put('/api/' + alias + '/' + db + '/data/' + key, {
      value: null,
      ttl: ttl
    })
  },
  modifyValue: (alias, db, key, value) => {
    return axios.put('/api/' + alias + '/' + db + '/data/' + key, {
      value: value,
      ttl: null
    })
  },
  deleteData: (alias, db, key) => {
    return axios.delete('/api/' + alias + '/' + db + '/data/' + key)
  },
  rename: (alias, db, key, newKey) => {
    return axios.put('/api/' + alias + '/' + db + '/data/' + key + '/rename', {
      newKey: newKey
    })
  }
}

export default apis
