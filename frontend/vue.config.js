module.exports = {
  outputDir: '../src/main/resources/templates',
  pages:{
    index:{
      entry: 'src/js/index.js',
      template: 'public/index.html',
      filename: 'index.html'
    },
    server_detail:{
      entry: 'src/js/server_detail.js',
      template: 'public/server_detail.html',
      filename: 'server_detail.html'
    }
  },
  runtimeCompiler: true,
  devServer: {
    proxy: {
      '/api': {
        target: 'http://localhost:8080'
      }
    },
    historyApiFallback: {
      rewrites: [
        { from: /^\/$/, to: '/index.html' },
        { from: /^\/(.*)/, to: '/server_detail.html' }
      ]
    }
  }
}
