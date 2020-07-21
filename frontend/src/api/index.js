import axios from '../plugins/axios'

let apis = {
  loadServerAliasList: () => {
    return axios.get('api/server_list')
  }
}

export default apis
