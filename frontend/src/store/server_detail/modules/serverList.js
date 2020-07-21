import serverList from '../../../api/server_detail/server_list'

// initial state
const state = {
  dbList: [],
  alias: ''
}

// getters
const getters = {}

// actions
const actions = {
  getDbList ({ commit }, alias) {
    commit('setAlias', alias)
    serverList.getDbList(alias)
      .then(response => {
        return Promise.resolve(response.data.data)
      })
      .then(dbMap => {
        let dbList = []
        for (let db in dbMap) {
          dbList.push({
            id: db,
            name: db,
            count: parseInt(dbMap[db]),
            isLoad: false,
            isDb: true,
            children: []
          })
        }
        commit('setDbList', dbList)
      })
  },
  getKeyList ({ commit, state }, setDb) {
    return new Promise((resolve) => {
      commit('setDbIsLoad', setDb)
      serverList.getDbKeyList(state.alias, setDb)
        .then(response => {
          commit('setDbKeyList', {
            setDb: setDb,
            dbKeyList: response.data.data
          })
          resolve(response)
        })
    })
  }
}

// mutations
const mutations = {
  setAlias (state, alias) {
    state.alias = alias
  },
  setDbList (state, dbList) {
    state.dbList = dbList
  },
  setDbIsLoad (state, setDb) {
    for (let index in state.dbList) {
      let db = parseInt(state.dbList[index].name)
      if (parseInt(setDb) === db) {
        state.dbList[index].isLoad = true
      }
    }
  },
  setDbKeyList (state, {setDb, dbKeyList}) {
    for (let db in state.dbList) {
      if (parseInt(setDb) === parseInt(db)) {
        state.dbList[db].children = []
        for (let key in dbKeyList) {
          state.dbList[db].children.push({
            id: setDb + '/' + dbKeyList[key],
            name: dbKeyList[key],
            db: setDb
          })
        }
      }
    }
  }
}

export default {
  namespaced: true,
  state,
  getters,
  actions,
  mutations
}
