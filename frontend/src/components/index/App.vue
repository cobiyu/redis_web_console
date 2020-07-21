<template>
  <v-app>
    <v-content>
      <v-container
        class="fill-height"
        fluid
      >
        <v-row
          align="center"
          justify="center"
        >
          <v-col
            cols="12"
            sm="8"
            md="4"
          >
            <v-card class="elevation-12">
              <v-toolbar
                color="primary"
                dark
                flat
              >
                <v-toolbar-title>
                  <v-icon>fas fa-lock</v-icon>
                  Redis Server List
                </v-toolbar-title>
                <v-spacer />
              </v-toolbar>
              <v-card-text>
                <v-list dense>
                  <v-list-item-group v-model="model" color="primary">
                    <v-list-item
                      v-for="(serverAlias, i) in serverAliasList"
                      :key="i"
                    >
                      <v-list-item-content>
                        <v-list-item-title
                          v-text="serverAlias"
                          @click="moveToServer(serverAlias)"
                          style="text-align: center"
                        ></v-list-item-title>
                      </v-list-item-content>
                    </v-list-item>
                  </v-list-item-group>
                </v-list>
              </v-card-text>
            </v-card>
          </v-col>
        </v-row>
      </v-container>
    </v-content>
  </v-app>
</template>

<script>
import api from '../../api'

export default {
  name: 'App',
  data () {
    return {
      serverAliasList: [],
      model: null
    }
  },
  mounted () {
    this.loadServerAliasList()
  },
  methods: {
    loadServerAliasList () {
      let self = this
      api.loadServerAliasList()
        .then(response => {
          self.serverAliasList = response.data.data
        })
    },
    moveToServer (serverAlias) {
      location.href = '/' + serverAlias
    }
  }
}
</script>

<style scoped>

</style>
