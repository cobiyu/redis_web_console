import valueApi from '../../../api/server_detail/valueApi'

// initial state
const state = {
  value: null,
  ttl: null
}

// getters
const getters = {}

// actions
const actions = {
  getValue ({ commit }, {alias, db, key}) {
    return valueApi.getValue(alias, db, key)
      .then(response => {
        commit('setValue', response.data.data.value)
        commit('setTtl', response.data.data.ttl)
      })
  },
  addKey ({ commit }, {alias, db, key, value, ttl}) {
    return valueApi.addKey(alias, db, key, value, ttl)
  },
  searchKey ({ commit }, {alias, db, key}) {
    return valueApi.getValue(alias, db, key)
  },
  modifyTtl ({ commit }, {alias, db, key, ttl}) {
    return valueApi.modifyTtl(alias, db, key, ttl)
      .then(response => {
        commit('setValue', response.data.data.value)
        commit('setTtl', response.data.data.ttl)
      })
  },
  modifyValue ({ commit }, {alias, db, key, value}) {
    return valueApi.modifyValue(alias, db, key, value)
      .then(response => {
        commit('setValue', response.data.data.value)
        commit('setTtl', response.data.data.ttl)
      })
  },
  deleteData ({ commit }, {alias, db, key}) {
    return valueApi.deleteData(alias, db, key)
      .then(response => {
        commit('setValue', null)
        commit('setTtl', null)
      })
  },
  rename ({ commit }, {alias, db, key, newKey}) {
    return valueApi.rename(alias, db, key, newKey)
      .then(response => {
        commit('setValue', null)
        commit('setTtl', null)
      })
  }
}

// mutations
const mutations = {
  setValue (state, value) {
    state.value = value
  },
  setTtl (state, ttl) {
    state.ttl = ttl
  }
}

export default {
  namespaced: true,
  state,
  getters,
  actions,
  mutations
}
